import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {LoginComponent} from "./login/login.component";
import {CurrentAccountsComponent} from "./current-accounts/current-accounts.component";
import {UsersComponent} from "./users/users.component";
import {AuthenticationGuard} from "./guards/authentication.guard";
import {AdminModeratorAuthorizationGuard} from "./guards/admin-moderator-authorization.guard";
import {SavingAccountsComponent} from "./saving-accounts/saving-accounts.component";
import {OperationsComponent} from "./operations/operations.component";
import {RegisterComponent} from "./register/register.component";
import {UserCurrAccOperComponent} from "./user-curr-acc-oper/user-curr-acc-oper.component";
import {UserAuthorizationGuard} from "./guards/user-authorization.guard";
import {UserSavAccOperComponent} from "./user-sav-acc-oper/user-sav-acc-oper.component";
import {UserDashboardComponent} from "./user-dashboard/user-dashboard.component";
import {ModeratorsComponent} from "./moderators/moderators.component";
import {AdminAuthorizationGuard} from "./guards/admin-authorization.guard";
import {NotAuthorizedComponent} from "./not-authorized/not-authorized.component";

const routes: Routes = [
  {path: "login", component: LoginComponent},
  {path: "register", component: RegisterComponent},
  {path: "forgot-password", component: RegisterComponent},
  {path: "accounts/currentAccounts", component: CurrentAccountsComponent, canActivate : [AuthenticationGuard, AdminModeratorAuthorizationGuard]},
  {path: "accounts/savingAccounts", component: SavingAccountsComponent, canActivate : [AuthenticationGuard, AdminModeratorAuthorizationGuard] },
  {path: "operations", component: OperationsComponent, canActivate: [AuthenticationGuard, AdminModeratorAuthorizationGuard] },
  {path: "moderators", component: ModeratorsComponent, canActivate: [AuthenticationGuard, AdminAuthorizationGuard] },
  {path: "users", canActivate: [AuthenticationGuard, AdminModeratorAuthorizationGuard] , component: UsersComponent},
  {path: "user/operations/currentAccount", canActivate: [AuthenticationGuard, UserAuthorizationGuard], component: UserCurrAccOperComponent},
  {path: "user/operations/savingAccount", canActivate: [AuthenticationGuard, UserAuthorizationGuard], component: UserSavAccOperComponent},
  {path: "user/dashboard", canActivate: [AuthenticationGuard, UserAuthorizationGuard], component: UserDashboardComponent},
  {path: "", redirectTo: "/login", pathMatch: "full"},
  {path: "notAuthorized", component: NotAuthorizedComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
