import {Component, OnInit} from '@angular/core';
import {CurrentAccountsRepositoryService} from "../services/current-accounts.repository.service";
import {AppStateService} from "../services/app-state.service";

@Component({
  selector: 'app-current-accounts',
  templateUrl: './current-accounts.component.html',
  styleUrl: './current-accounts.component.css'
})
export class CurrentAccountsComponent implements OnInit{
  constructor(private currentAccountsService : CurrentAccountsRepositoryService,
              public appState : AppStateService) {
  }

  searchCurrentAccounts() {
    this.currentAccountsService.searchCurrentAccounts({});
  }

  ngOnInit(): void {
    this.searchCurrentAccounts();
  }

  goToPage(number: number) {
    this.appState.setCurrentAccountsState({currentPage:number});
    this.searchCurrentAccounts();
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
