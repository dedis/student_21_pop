package message

import (
	"encoding/json"
	"fmt"
	"testing"

	"github.com/stretchr/testify/require"
)

func TestMessage_Marshal_CreateLAO(t *testing.T) {
	data, err := NewCreateLAOData(
		"test",
		Timestamp(123),
		PublicKey([]byte{4, 5, 6}),
		[]PublicKey{PublicKey([]byte{1, 1, 1})},
	)
	require.NoError(t, err)

	msg := Message{
		MessageID:         []byte{7, 8, 9},
		Signature:         []byte{1, 2, 3},
		Sender:            []byte{4, 5, 6},
		WitnessSignatures: []PublicKeySignaturePair{},
		Data:              data,
	}

	buf, err := json.Marshal(msg)
	require.NoError(t, err)

	fmt.Printf("marshaled: %s", buf)
}

func TestMessage_Unmarshal_CreateLAO(t *testing.T) {
	msgData := `
	{"message_id":"BwgJ","data":"eyJhY3Rpb24iOiJjcmVhdGUiLCJvYmplY3QiOiJsYW8iLCJpZCI6Imh3L2xqK3pBa0JjMGJaTVpKdThWK2xrcE1MZHVnQktIWnBaSHJaMDl5RU09IiwibmFtZSI6InRlc3QiLCJjcmVhdGlvbiI6MTIzLCJvcmdhbml6ZXIiOiJCQVVHIiwid2l0bmVzc2VzIjpbIkFRRUIiXX0=","sender":"BAUG","signature":"AQID","witness_signatures":[]}
	`

	msg := &Message{}
	err := json.Unmarshal([]byte(msgData), msg)
	require.NoError(t, err)

	data, err := NewCreateLAOData(
		"test",
		Timestamp(123),
		PublicKey([]byte{4, 5, 6}),
		[]PublicKey{PublicKey([]byte{1, 1, 1})},
	)
	require.NoError(t, err)
	require.Equal(t, data, msg.Data)
}
