package hub

import (
	"encoding/base64"
	"encoding/json"
	"os"
	"student20_pop"
	"student20_pop/message"
	"testing"
	"time"

	"github.com/stretchr/testify/require"
	"go.dedis.ch/kyber/v3"
	"go.dedis.ch/kyber/v3/sign/schnorr"
	"golang.org/x/xerrors"
)

type keypair struct {
	public    kyber.Point
	publicBuf message.PublicKey
	private   kyber.Scalar
}

var witnesses []keypair

var suite = student20_pop.Suite

var organizerKeyPair keypair

func generateKeyPair() (kyber.Point, message.PublicKey, kyber.Scalar, error) {
	secret := suite.Scalar().Pick(suite.RandomStream())
	point := suite.Point()
	point = point.Mul(secret, point)

	pkbuf, err := point.MarshalBinary()
	if err != nil {
		return nil, nil, nil, xerrors.Errorf("failed to create keypair: %v", err)
	}

	return point, pkbuf, secret, nil
}

func TestMain(m *testing.M) {
	// generate witness public keys
	for i := 0; i < 10; i++ {
		pk, pkbuf, secret, err := generateKeyPair()
		if err != nil {
			panic(err)
		}
		witnesses = append(witnesses, keypair{public: pk, publicBuf: pkbuf, private: secret})
	}

	pk, pkbuf, private, err := generateKeyPair()
	if err != nil {
		panic(err)
	}

	organizerKeyPair = keypair{public: pk, publicBuf: pkbuf, private: private}

	res := m.Run()
	os.Exit(res)
}

func TestOrganizer_CreateLAO(t *testing.T) {
	h := NewOrganizerHub(organizerKeyPair.public)

	// Create a LAO given a CreateLAO message
	creation := time.Now()

	witnessPks := make([]message.PublicKey, 3)
	for i := 0; i < 3; i++ {
		witnessPks = append(witnessPks, witnesses[i].publicBuf)
	}

	data, err := message.NewCreateLAOData("test", message.Timestamp(creation.Unix()), organizerKeyPair.publicBuf, witnessPks)
	require.NoError(t, err)

	dataBuf, err := json.Marshal(data)
	require.NoError(t, err)

	signature, err := schnorr.Sign(suite, organizerKeyPair.private, dataBuf)
	require.NoError(t, err)

	msg, err := message.NewMessage(organizerKeyPair.publicBuf, signature, []message.PublicKeySignaturePair{}, data)
	require.NoError(t, err)

	createLao := message.Publish{
		ID:     1,
		Method: "publish",
		Params: message.Params{
			Channel: "/root",
			Message: msg,
		},
	}

	h.createLao(createLao)

	require.Equal(t, 1, len(h.channelByID))

	key := base64.StdEncoding.EncodeToString(data.ID)
	_, ok := h.channelByID[key]
	require.True(t, ok)
}
