import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LoginComponent } from './login/login.component';
import { NavbarComponent } from './navbar/navbar.component';
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import { CurrentAccountsComponent } from './current-accounts/current-accounts.component';
import { UsersComponent } from './users/users.component';
import {HTTP_INTERCEPTORS, HttpClientModule} from "@angular/common/http";
import {HttpAppInterceptor} from "./services/http-app.interceptor";
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import {MatIcon} from "@angular/material/icon";
import {MatButton} from "@angular/material/button";
import { SavingAccountsComponent } from './saving-accounts/saving-accounts.component';
import { OperationsComponent } from './operations/operations.component';
import {MatFormField, MatLabel} from "@angular/material/form-field";
import {MatOption, MatSelect} from "@angular/material/select";
import {MatDialogActions, MatDialogClose, MatDialogContent, MatDialogTitle} from "@angular/material/dialog";
import {MatRadioButton, MatRadioGroup} from "@angular/material/radio";

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    NavbarComponent,
    CurrentAccountsComponent,
    UsersComponent,
    SavingAccountsComponent,
    OperationsComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule,
    MatIcon,
    MatButton,
    MatLabel,
    MatSelect,
    MatOption,
    MatFormField,
    MatDialogTitle,
    MatDialogContent,
    MatRadioGroup,
    MatRadioButton
  ],
  providers: [
    {provide: HTTP_INTERCEPTORS, useClass: HttpAppInterceptor, multi: true},
    provideAnimationsAsync()
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
