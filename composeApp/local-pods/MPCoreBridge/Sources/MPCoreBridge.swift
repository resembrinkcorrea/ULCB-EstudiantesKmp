import Foundation
import UIKit
import CoreMethods

// Permite pasar closures @objc @escaping a través de un Task sin violar Sendable
private final class CallbackHolder: @unchecked Sendable {
    let onSuccess: (String) -> Void
    let onError: (String) -> Void
    init(_ onSuccess: @escaping (String) -> Void, _ onError: @escaping (String) -> Void) {
        self.onSuccess = onSuccess
        self.onError = onError
    }
}


/// ObjC-accessible wrapper around the MercadoPago Core Methods Swift SDK.
/// Exposes PCI text fields, tokenization, and installments to Kotlin/Native via cinterop.
@objc(MPCoreBridge)
@objcMembers
public class MPCoreBridge: NSObject, @unchecked Sendable {

    // MARK: - PCI Fields

    @objc public let cardNumberField: CardNumberTextField
    @objc public let expirationField: ExpirationDateTextfield
    @objc public let securityCodeField: SecurityCodeTextField

    // MARK: - Callbacks (set from Kotlin)

    @objc public var onBinChanged: ((String) -> Void)?
    @objc public var onBinCleared: (() -> Void)?

    private let coreMethods: CoreMethods
    private nonisolated(unsafe) static var sdkInitialized = false

    // MARK: - Init

    @objc public override init() {
        self.cardNumberField = CardNumberTextField()
        self.expirationField = ExpirationDateTextfield()
        self.securityCodeField = SecurityCodeTextField()
        self.coreMethods = CoreMethods()
        super.init()

        cardNumberField.onBinChanged = { [weak self] bin in
            guard let self = self else { return }
            if bin.isEmpty {
                self.onBinCleared?()
            } else {
                self.onBinChanged?(bin)
            }
        }
    }

    // MARK: - SDK Initialization

    @objc public static func initializeSDK(publicKey: String) {
        guard !publicKey.isEmpty else { return }
        let config = MercadoPagoSDK.Configuration(
            publicKey: publicKey,
            country: .PER
        )
        if !sdkInitialized {
            MercadoPagoSDK.shared.initialize(config)
            sdkInitialized = true
        } else {
            MercadoPagoSDK.shared.setNewConfiguration(config)
        }
    }

    // MARK: - Token Generation

    @objc public func generateToken(
        cardHolderName: String,
        docTypeName: String,
        docNumber: String,
        onSuccess: @escaping (String) -> Void,
        onError: @escaping (String) -> Void
    ) {
        let holder = CallbackHolder(onSuccess, onError)
        Task {
            do {
                let docTypes = try await coreMethods.identificationTypes()
                let docType = docTypes.first { $0.name.uppercased() == docTypeName.uppercased() }

                let cardToken: CardToken
                if let docType = docType {
                    cardToken = try await coreMethods.createToken(
                        cardNumber: cardNumberField,
                        expirationDate: expirationField,
                        securityCode: securityCodeField,
                        documentType: docType,
                        documentNumber: docNumber,
                        cardHolderName: cardHolderName
                    )
                } else {
                    cardToken = try await coreMethods.createToken(
                        cardNumber: cardNumberField,
                        expirationDate: expirationField,
                        securityCode: securityCodeField,
                        cardHolderName: cardHolderName
                    )
                }

                await MainActor.run { holder.onSuccess(cardToken.token) }
            } catch {
                await MainActor.run { holder.onError(error.localizedDescription) }
            }
        }
    }

    // MARK: - Installments

    // onSuccess recibe JSON: {"payment_method_id":"...","cuotas":[...]}
    @objc public func fetchInstallments(
        bin: String,
        amount: Double,
        onSuccess: @escaping (String) -> Void,
        onError: @escaping (String) -> Void
    ) {
        let holder = CallbackHolder(onSuccess, onError)
        Task {
            do {
                let result = try await coreMethods.installments(amount: amount, bin: bin)
                guard let first = result.first else {
                    await MainActor.run { holder.onError("No se encontraron cuotas") }
                    return
                }
                let json = buildInstallmentsJson(paymentMethodId: first.paymentMethodId, payerCosts: first.payerCosts)
                await MainActor.run { holder.onSuccess(json) }
            } catch {
                await MainActor.run { holder.onError(error.localizedDescription) }
            }
        }
    }

    private func buildInstallmentsJson(paymentMethodId: String, payerCosts: [Installment.PayerCost]) -> String {
        let cuotas = payerCosts.map { pc -> String in
            let n = pc.installments
            let amt = pc.installmentAmount
            let total = pc.totalAmount
            let rate = pc.installmentRate
            let msg = n == 1
                ? String(format: "1 cuota de S/ %.2f (S/ %.2f)", amt, total)
                : String(format: "%d cuotas de S/ %.2f (S/ %.2f)", n, amt, total)
            return "{\"installments\":\(n),\"installment_rate\":\(rate),\"recommended_message\":\"\(msg)\",\"installment_amount\":\(amt),\"total_amount\":\(total)}"
        }.joined(separator: ",")
        return "{\"payment_method_id\":\"\(paymentMethodId)\",\"cuotas\":[\(cuotas)]}"
    }
}
