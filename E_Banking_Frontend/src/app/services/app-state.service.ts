import { Injectable } from '@angular/core';
import {CurrentAccount} from "../models/current-account.model";
import {SavingAccount} from "../models/saving-account.model";

@Injectable({
  providedIn: 'root'
})
export class AppStateService {

  public authState:any={
    isAuthenticated :false,
    username:"",
    id:"",
    email:"",
    roles :[],
    token :"",
    status :"",
    errorMessage :"",
  }

  public usersState :any={
    users:[],
    totalPages:0,
    keyword:"",
    currentPage : 0,
    pageSize: 6,
    status :"",
    errorMessage :""
  }

  public currentAccountsState :any={
    currentAccounts:[],
    totalPages:0,
    currentPage : 0,
    pageSize: 6,
    status :"",
    errorMessage :""
  }

  public moderatorsState :any={
    moderators:[],
    totalPages:0,
    currentPage : 0,
    pageSize: 4,
    status :"",
    errorMessage :""
  }

  public setModeratorsState(state:any){
    this.moderatorsState={...this.moderatorsState, ...state};
  }

  public currentAccountDetails !: CurrentAccount;
  public savingAccountDetails !: SavingAccount;

  public setCurrentAccountDetails(currentAccountDetails: CurrentAccount) {
    this.currentAccountDetails = currentAccountDetails;
  }

  public setSavingAccountDetails(savingAccountDetails: SavingAccount) {
    this.savingAccountDetails = savingAccountDetails;
  }

  public savingAccountsState :any={
    savingAccounts:[],
    totalPages:0,
    currentPage : 0,
    pageSize: 6,
    status :"",
    errorMessage :""
  }

  public operationsState :any={
    operations:[],
    totalPages:0,
    currentPage : 0,
    pageSize: 8,
    status :"",
    errorMessage :""
  }

  public userAccounts : any={
    currentAccount :CurrentAccount,
    savingAccount :SavingAccount
  }

  public setUserAccounts(state:any){
    this.userAccounts={...this.userAccounts, ...state};
  }

  public getUserByUserId(userId : number){
    return this.usersState.users.find((user:any)=>user.id===userId);
  }

  public setOperationsState(state:any){
    this.operationsState={...this.operationsState, ...state};
  }

  public setAuthState(state:any){
    this.authState={...this.authState, ...state};
  }

  public setUsersState(state:any){
    this.usersState={...this.usersState, ...state};
  }

  public setCurrentAccountsState(state:any){
    this.currentAccountsState={...this.currentAccountsState, ...state};
  }

  public setSavingAccountsState(state:any){
    this.savingAccountsState={...this.savingAccountsState, ...state};
  }

  constructor() { }
}
