import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {LoginComponent} from "./login/login.component";
import {CurrentAccountsComponent} from "./current-accounts/current-accounts.component";
import {UsersComponent} from "./users/users.component";
import {AuthenticationGuard} from "./guards/authentication.guard";
import {AdminModeratorAuthorizationGuard} from "./guards/admin-moderator-authorization.guard";
import {SavingAccountsComponent} from "./saving-accounts/saving-accounts.component";
import {OperationsComponent} from "./operations/operations.component";

const routes: Routes = [
  {path: "login", component: LoginComponent},
  {path: "accounts/currentAccounts", component: CurrentAccountsComponent, canActivate : [AuthenticationGuard, AdminModeratorAuthorizationGuard]},
  {path: "accounts/savingAccounts", component: SavingAccountsComponent, canActivate : [AuthenticationGuard, AdminModeratorAuthorizationGuard] },
  {path: "operations", component: OperationsComponent, canActivate: [AuthenticationGuard, AdminModeratorAuthorizationGuard] },
  {path: "users", canActivate: [AuthenticationGuard, AdminModeratorAuthorizationGuard] , component: UsersComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
