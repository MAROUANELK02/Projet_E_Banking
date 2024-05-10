import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {AppStateService} from "./app-state.service";
import {ApiResponse} from "../models/api-response.model";
import {Operation} from "../models/operation.model";
import {Observable, tap} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class OperationsRepositoryService {
  host : string = "http://localhost:5000/api/operations/"
  host1 : string = "http://localhost:5000/api/accounts/"

  constructor(private http : HttpClient,
              private appState : AppStateService) { }

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
}
