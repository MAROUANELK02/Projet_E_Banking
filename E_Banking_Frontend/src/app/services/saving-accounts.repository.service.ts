import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {AppStateService} from "./app-state.service";
import {ApiResponse} from "../models/api-response.model";
import {SavingAccount} from "../models/saving-account.model";

@Injectable({
  providedIn: 'root'
})
export class SavingAccountsRepositoryService {

  host : string = "http://localhost:5000/api/accounts/saving";

  constructor(private http : HttpClient, private appState : AppStateService) {
  }

  searchSavingAccounts({
                          currentPage = this.appState.savingAccountsState.currentPage,
                          size = this.appState.savingAccountsState.pageSize
                        }){
    this.appState.savingAccountsState.status = "loading";
    this.http.get<ApiResponse<SavingAccount>>(this.host , {
      params : {
        page : currentPage,
        size : size
      },
    }).subscribe({
      next : (resp : ApiResponse<SavingAccount>)=>{
        let savingAccounts = resp.content;
        let totalPages = resp.totalPages;
        this.appState.setSavingAccountsState({
          savingAccounts, totalPages, currentPage, status:"LOADED", errorMessage:""
        })
      },
      error : (err)=>{
        this.appState.setSavingAccountsState({status:"ERROR", errorMessage:err.statusText});
      }
    });

  }

  ngOnInit(): void {
    this.searchSavingAccounts({});
  }
}
