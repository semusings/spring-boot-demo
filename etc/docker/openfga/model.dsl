model
  schema 1.1

type customer
type user

type order
  relations
    define owner: [customer]
    define viewer: [user, customer]

type product
  relations
    define manager: [user]
    define viewer: [user, customer]

type payment
  relations
    define owner: [customer]
    define viewer: [user, customer]