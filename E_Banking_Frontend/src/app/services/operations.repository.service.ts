import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {AppStateService} from "./app-state.service";
import {ApiResponse} from "../models/api-response.model";
import {Operation} from "../models/operation.model";
import {Observable, tap} from "rxjs";
import {CurrentAccount} from "../models/current-account.model";
import {SavingAccount} from "../models/saving-account.model";

@Injectable({
  providedIn: 'root'
})
export class OperationsRepositoryService {
  host : string = "http://localhost:5000/api/operations/"
  host1 : string = "http://localhost:5000/api/accounts/"
  customersHost : string = "http://localhost:5000/api/customers/"

  constructor(private http : HttpClient,
              private appState : AppStateService) { }

  transaction(accountIdSource:string, accountIdDestionation:string, amount:number) {
    this.appState.operationsState.status = "LOADING";
    return this.http.post(this.host+"transaction", {}, {
      params: {
        ribSource: accountIdSource,
        ribDestination: accountIdDestionation,
        amount: amount
      }
    }).pipe(
      tap({
        next : ()=>{
          this.appState.setOperationsState({status:"SUCCESS", errorMessage:""});
        },
        error : (err)=>{
          this.appState.setOperationsState({status:"ERROR", errorMessage:err.statusText});
        }
      })
    );
  }

  searchOperations({
                    currentPage = this.appState.operationsState.currentPage,
                    size = this.appState.operationsState.pageSize
                  }){
    this.appState.operationsState.status = "loading";
    this.http.get<ApiResponse<Operation>>(this.host , {
      params : {
        page : currentPage,
        size : size
      },
    }).subscribe({
      next : (resp : ApiResponse<Operation>)=>{
        let operations = resp.content;
        let totalPages = resp.totalPages;
        this.appState.setOperationsState({
          operations, totalPages, currentPage, status:"LOADED", errorMessage:""
        })
      },
      error : (err)=>{
        this.appState.setOperationsState({status:"ERROR", errorMessage:err.statusText});
      }
    });
  }

  debitAccount(operation : Operation): Observable<Operation> {
    this.appState.operationsState.status = "LOADING";
    return this.http.post<Operation>(this.host1+"debit", operation).pipe(
      tap({
        next : ()=>{
          this.appState.setOperationsState({status:"SUCCESS", errorMessage:""});
          this.searchOperations({});
        },
        error : (err)=>{
          this.appState.setOperationsState({status:"ERROR", errorMessage:err.statusText});
        }
      })
    );
  }

  creditAccount(operation : Operation): Observable<Operation> {
    this.appState.operationsState.status = "LOADING";
    return this.http.post<Operation>(this.host1+"credit", operation).pipe(
      tap({
        next : ()=>{
          this.appState.setOperationsState({status:"SUCCESS", errorMessage:""});
          this.searchOperations({});
        },
        error : (err)=>{
          this.appState.setOperationsState({status:"ERROR", errorMessage:err.statusText});
        }
      })
    );
  }

  ngOnInit(): void {
    this.searchOperations({});
  }

  getOperationDetails(id: number) {
    return this.http.get<Operation>(this.host+id);
  }

  cancelOperation(id: number) {
    this.appState.operationsState.status = "LOADING";
    return this.http.delete(this.host+id).pipe(
      tap({
        next : ()=>{
          this.appState.setOperationsState({status:"SUCCESS", errorMessage:""});
          this.searchOperations({});
        },
        error : (err)=>{
          this.appState.setOperationsState({status:"ERROR", errorMessage:err.statusText});
        }
      })
    );
  }

  searchCurrAccId(customerId: string){
    return this.http.get<CurrentAccount>(this.customersHost + "currentAccount/" + customerId)
  }

  searchCurrAccOperationsHistory(accountId: string) : Observable<ApiResponse<CurrentAccount>> {
    return this.http.get<ApiResponse<CurrentAccount>>(this.host1 + accountId + "/operations", {
      params : {
        page : this.appState.operationsState.currentPage,
        size : this.appState.operationsState.pageSize
      }
    });
  }

  searchSavAccId(customerId: string){
    return this.http.get<SavingAccount>(this.customersHost + "savingAccount/" + customerId);
  }

  searchSavAccOperationsHistory(accountId: string) : Observable<ApiResponse<SavingAccount>> {
    return this.http.get<ApiResponse<SavingAccount>>(this.host1 + accountId + "/operations", {
      params : {
        page : this.appState.operationsState.currentPage,
        size : this.appState.operationsState.pageSize
      }
    });
  }

}
