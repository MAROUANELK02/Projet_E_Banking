import { Component } from '@angular/core';
import {AppStateService} from "../services/app-state.service";
import {SavingAccountsRepositoryService} from "../services/saving-accounts.repository.service";

@Component({
  selector: 'app-saving-accounts',
  templateUrl: './saving-accounts.component.html',
  styleUrl: './saving-accounts.component.css'
})
export class SavingAccountsComponent {
  constructor(private savingAccountsService : SavingAccountsRepositoryService,
              public appState : AppStateService) {
  }

  searchSavingAccounts() {
    this.savingAccountsService.searchSavingAccounts({});
  }

  ngOnInit(): void {
    this.searchSavingAccounts();
  }

  goToPage(number: number) {
    this.appState.setSavingAccountsState({currentPage:number});
    this.searchSavingAccounts();
  }

  changeStatus(id: number) {

  }

  closeAccount(id : number) {

  }

  debitAccount(id : number) {

  }

  creditAccount(id : number) {

  }

}
