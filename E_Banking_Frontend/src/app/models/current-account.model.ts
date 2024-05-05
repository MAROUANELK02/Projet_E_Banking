import {User} from "./user.model";

export class CurrentAccount {
  id!: string;
  balance!: number;
  status!: string;
  closed!: boolean;
  overDraft!: number;
  customerDTO! : User;
}
