import {Component, OnInit} from '@angular/core';
import {AppStateService} from "../services/app-state.service";
import {OperationsRepositoryService} from "../services/operations.repository.service";

@Component({
  selector: 'app-user-curr-acc-oper',
  templateUrl: './user-curr-acc-oper.component.html',
  styleUrl: './user-curr-acc-oper.component.css'
})
export class UserCurrAccOperComponent implements OnInit{

  constructor(public appState : AppStateService,
              private operationService: OperationsRepositoryService) {
  }

  goToPage(index: number) {
    this.appState.operationsState.currentPage = index;
    this.searchOperationsHistory();
  }

  searchOperationsHistory() {
    this.appState.operationsState.status = "LOADING";
    this.operationService.searchCurrAccId(this.appState.authState.id).subscribe({
      next: (currentAccount) => {
        this.operationService.searchCurrAccOperationsHistory(currentAccount.id).subscribe({
          next: (response ) => {
            let operations = response.content;
            let totalPages = response.totalPages;
            this.appState.setOperationsState({operations, totalPages, status: "LOADED", errorMessage: ""});
          },
          error: (err) => {
            this.appState.setOperationsState({status: "ERROR", errorMessage: err.statusText});
          }
        });
      },
      error: (err) => {
        this.appState.setOperationsState({status: "ERROR", errorMessage: err.statusText});
      }
    });
  }

  ngOnInit(): void {
    this.searchOperationsHistory();
  }
}
