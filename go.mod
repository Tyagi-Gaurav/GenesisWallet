module github.com/wallet

go 1.19

require (
	github.com/google/uuid v1.3.0
	golang.org/x/net v0.0.0-20220920152717-4a395b0a80a1
)

require golang.org/x/text v0.3.7 // indirect

replace github.com/wallet/user => ./user/

replace github.com/wallet/utils => ./utils/
