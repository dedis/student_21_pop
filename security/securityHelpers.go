// security contains all the function to perform checks on Data, from timestamp coherence to signature validation
package security

import (
	ed "crypto/ed25519"
	"crypto/sha256"
	"encoding/json"
	"log"
	"student20_pop/lib"
	"student20_pop/parser"
)

const MaxPropagationDelay = 5
const MaxClockDifference = 2

// VerifySignature checks that Sign(itemToVerify) corresponds to the given signature
func VerifySignature(publicKey []byte, itemToVerify []byte, signature []byte) error {
	//check the size of the key as it will panic if we plug it in Verify
	if len(publicKey) != ed.PublicKeySize {
		return lib.ErrRequestDataInvalid
	}
	if ed.Verify(publicKey, itemToVerify, signature) {
		return nil
	}
	//invalid signature
	return lib.ErrRequestDataInvalid
}

// VerifyWitnessSignatures verify that for each WitnessSignatureEnc, the public key belongs to authorizedWitnesses and
// verifies that the signature is correct
func VerifyWitnessSignatures(authorizedWitnesses [][]byte, witnessSignaturesEnc []json.RawMessage, messageId []byte) error {
	for _, item := range witnessSignaturesEnc {
		witnessSignature, err := parser.ParseWitnessSignature(item)
		if err != nil {
			log.Println("couldn't unMarshal one of the Item in WitnessSignatures")
			return lib.ErrInvalidResource
		}
		//We check that the signature belong to an assigned witness
		_, isAssigned := lib.FindByteArray(authorizedWitnesses, witnessSignature.WitnessKey)
		if !isAssigned {
			// if one witness is unauthorized, the whole message is refused
			log.Printf("witness public key not recognized")
			return lib.ErrRequestDataInvalid
		}
		//then we check correctness of the signature
		err = VerifySignature(witnessSignature.WitnessKey, messageId, witnessSignature.Signature)
		if err != nil {
			return err
		}
	}
	return nil
}

// HashItems is a one-liner to hash every element we have represented in a JSON array with escaped values
// note : most of the values we hash are either numbers or B64 strings, so most of the time the escape function is useless.
func HashItems(itemsToHash []string) []byte {
	hash := sha256.Sum256([]byte(lib.ArrayRepresentation(itemsToHash)))
	return hash[:]
}