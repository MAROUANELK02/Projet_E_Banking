import {Injectable, OnInit} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {AppStateService} from "./app-state.service";
import {ApiResponse} from "../models/api-response.model";
import {User} from "../models/user.model";

@Injectable({
  providedIn: 'root'
})
export class CustomersRepositoryService implements OnInit{
  host : string = "http://localhost:5000/api/customers/";

  constructor(private http : HttpClient, private appState : AppStateService) {
  }

  searchCustomers({
    keyword = this.appState.usersState.keyword,
    currentPage = this.appState.usersState.currentPage,
    size = this.appState.usersState.pageSize
  }){
    this.appState.usersState.status = "loading";
    this.http.get<ApiResponse<User>>(this.host , {
      params : {
        keyword : keyword,
        page : currentPage,
        size : size
      },
    }).subscribe({
      next : (resp : ApiResponse<User>)=>{
        let users = resp.content;
        let totalPages = resp.totalPages;
        this.appState.setUsersState({
          users, totalPages, currentPage, keyword, status:"LOADED", errorMessage:""
        })
      },
      error : (err)=>{
        this.appState.setUsersState({status:"ERROR", errorMessage:err.statusText});
      }
    });

    }

  ngOnInit(): void {
    this.searchCustomers({});
  }

  createCustomer(user : User) {
    this.appState.usersState.status = "LOADING";
    this.http.post<User>(this.host, user).subscribe({
      next : ()=>{
        this.appState.setUsersState({status:"SUCCESS", errorMessage:""});
        this.searchCustomers({});
      },
      error : (err)=>{
        this.appState.setUsersState({status:"ERROR", errorMessage:err.statusText});
      }
    });
  }

  updateCustomer(user: User) {
    this.appState.usersState.status = "LOADING";
    this.http.put<User>(this.host+user.id, user).subscribe({
      next: () =>{
      this.appState.setUsersState({status:"SUCCESS", errorMessage:""});
      this.searchCustomers({});
      },
      error : (err)=>{
        this.appState.setUsersState({status:"ERROR", errorMessage:err.statusText});
      }
    });
  }

  deleteCustomer(id: number) {
    this.appState.usersState.status = "LOADING";
    this.http.delete(this.host+id).subscribe({
      next: () =>{
        this.appState.setUsersState({status:"SUCCESS", errorMessage:""});
        this.searchCustomers({});
      },
      error : (err)=>{
        this.appState.setUsersState({status:"ERROR", errorMessage:err.statusText});
      }
    });
  }

}
