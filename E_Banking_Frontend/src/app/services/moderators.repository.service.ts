import { Injectable } from '@angular/core';
import {AppStateService} from "./app-state.service";
import {HttpClient} from "@angular/common/http";
import {Moderator} from "../models/moderator.model";
import {User} from "../models/user.model";
import {ApiResponse} from "../models/api-response.model";

@Injectable({
  providedIn: 'root'
})
export class ModeratorsRepositoryService {
  host : string = "http://localhost:5000/api/moderator/";

  constructor(private appState : AppStateService,
              private http : HttpClient) { }

  addModerator(moderator : Moderator) {
    this.appState.moderatorsState.status = "LOADING";
    this.http.post<User>(this.host + "addModerator", moderator).subscribe({
      next : ()=>{
        this.appState.setModeratorsState({status:"SUCCESS", errorMessage:""});
        this.searchModerators({});
      },
      error : (err)=>{
        this.appState.setModeratorsState({status:"ERROR", errorMessage:err.statusText});
      }
    });
  }

  public searchModerators({
                             currentPage = this.appState.moderatorsState.currentPage,
                             size = this.appState.moderatorsState.pageSize
                           }) {
    this.appState.moderatorsState.status = "loading";
    this.http.get<ApiResponse<Moderator>>(this.host + "all", {
      params : {
        page : currentPage,
        size : size
      },
    }).subscribe({
      next : (resp : ApiResponse<Moderator>)=>{
        let moderators = resp.content;
        let totalPages = resp.totalPages;
        this.appState.setModeratorsState({
          moderators, totalPages, currentPage, status:"LOADED", errorMessage:""
        })
      },
      error : (err)=>{
        this.appState.setModeratorsState({status:"ERROR", errorMessage:err.statusText});
      }
    });
  }

  deleteModerator(id: number) {
    this.appState.moderatorsState.status = "LOADING";
    this.http.delete(this.host+id).subscribe({
      next: () =>{
        this.appState.setModeratorsState({status:"SUCCESS", errorMessage:""});
        this.searchModerators({});
      },
      error : (err)=>{
        this.appState.setModeratorsState({status:"ERROR", errorMessage:err.statusText});
      }
    });
  }
}
