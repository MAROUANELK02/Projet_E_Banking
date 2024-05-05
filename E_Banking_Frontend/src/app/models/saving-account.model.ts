import {User} from "./user.model";

export class SavingAccount {
  id!: string;
  balance!: number;
  status!: string;
  closed!: boolean;
  interestRate!: number;
  customerDTO! : User;
}
