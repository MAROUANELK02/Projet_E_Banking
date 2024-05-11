import {Injectable, OnInit} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {AppStateService} from "./app-state.service";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class AuthRepositoryService {
  host:string ="http://localhost:5000/api/auth/signin";
  registerHost:string ="http://localhost:5000/api/register";

  constructor(private http : HttpClient, private appState : AppStateService) { }

  async login(username:string, password:string) :Promise<any> {
    try {
      let data:any = await this.http.post(this.host, {
        username: username,
        password: password
      }).toPromise();
      if (data.token){
        this.appState.setAuthState({
          isAuthenticated : true,
          username : data.username,
          id:data.id,
          email:data.email,
          roles : data.roles,
          token :data.token
        })
        return Promise.resolve(true);
      } else {
        return Promise.reject("Bad Credentials");
      }
    } catch (e) {
      return Promise.reject("Network error");
    }
  }

  public  logout(){
    this.appState.setAuthState({
      isAuthenticated : false,
      username :"",
      id:"",
      email:"",
      roles :[],
      token :"",
    })
  }

  checkEmail(email : string) : Observable<any> {
    return this.http.post(this.registerHost+"/sendEmail", {email:email});
  }

  register(email : string, password : string, confirmPassword: string) : Observable<any> {
    return this.http.put(this.registerHost+"/changePassword", {email:email, password:password, confirmedPassword:confirmPassword});
  }
}
