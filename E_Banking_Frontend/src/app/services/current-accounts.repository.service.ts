import {Injectable, OnInit} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {AppStateService} from "./app-state.service";
import {ApiResponse} from "../models/api-response.model";
import {CurrentAccount} from "../models/current-account.model";
import {Observable, tap} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class CurrentAccountsRepositoryService implements OnInit{
  host : string = "http://localhost:5000/api/accounts/";

  constructor(private http : HttpClient, private appState : AppStateService) {
  }

  searchCurrentAccounts({
                    currentPage = this.appState.currentAccountsState.currentPage,
                    size = this.appState.currentAccountsState.pageSize
                  }){
    this.appState.currentAccountsState.status = "loading";
    this.http.get<ApiResponse<CurrentAccount>>(this.host+"current" , {
      params : {
        page : currentPage,
        size : size
      },
    }).subscribe({
      next : (resp : ApiResponse<CurrentAccount>)=>{
        let currentAccounts = resp.content;
        let totalPages = resp.totalPages;
        this.appState.setCurrentAccountsState({
          currentAccounts, totalPages, currentPage, status:"LOADED", errorMessage:""
        })
      },
      error : (err)=>{
        this.appState.setCurrentAccountsState({status:"ERROR", errorMessage:err.statusText});
      }
    });
  }

  ngOnInit(): void {
    this.searchCurrentAccounts({});
  }

  saveAccount(currentAccount : CurrentAccount, customerId : number) {
    this.appState.currentAccountsState.status = "LOADING";
    this.http.post<CurrentAccount>(this.host+"create/currentAccount/"+customerId, currentAccount).subscribe({
      next : ()=>{
        this.appState.setCurrentAccountsState({status:"SUCCESS", errorMessage:""});
        this.searchCurrentAccounts({});
      },
      error : (err)=>{
        this.appState.setCurrentAccountsState({status:"ERROR", errorMessage:err.statusText});
      }
    });
  }

  activeAccount(accountId : number) : Observable<CurrentAccount> {
    this.appState.currentAccountsState.status = "LOADING";
    return this.http.put<CurrentAccount>(this.host + accountId + "/activated", {}).pipe(
      tap({
        next : ()=>{
          this.appState.setCurrentAccountsState({status:"SUCCESS", errorMessage:""});
          this.searchCurrentAccounts({});
        },
        error : (err)=>{
          this.appState.setCurrentAccountsState({status:"ERROR", errorMessage:err.statusText});
        }
      })
    );
  }

  suspendAccount(accountId : number) : Observable<CurrentAccount> {
    this.appState.currentAccountsState.status = "LOADING";
    return this.http.put<CurrentAccount>(this.host + accountId + "/suspended", {}).pipe(
      tap({
        next : ()=>{
          this.appState.setCurrentAccountsState({status:"SUCCESS", errorMessage:""});
          this.searchCurrentAccounts({});
        },
        error : (err)=>{
          this.appState.setCurrentAccountsState({status:"ERROR", errorMessage:err.statusText});
        }
      })
    );
  }

  closeAccount(accountId : number): Observable<CurrentAccount> {
    this.appState.currentAccountsState.status = "LOADING";
    return this.http.delete<CurrentAccount>(this.host + accountId, {}).pipe(
      tap({
        next : ()=>{
          this.appState.setCurrentAccountsState({status:"SUCCESS", errorMessage:""});
          this.searchCurrentAccounts({});
        },
        error : (err)=>{
          this.appState.setCurrentAccountsState({status:"ERROR", errorMessage:err.statusText});
        }
      })
    );
  }

}
