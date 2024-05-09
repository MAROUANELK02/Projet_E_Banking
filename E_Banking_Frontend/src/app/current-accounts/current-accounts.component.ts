import {Component, OnInit} from '@angular/core';
import {CurrentAccountsRepositoryService} from "../services/current-accounts.repository.service";
import {AppStateService} from "../services/app-state.service";
import {FormBuilder, FormGroup} from "@angular/forms";
import {Operation} from "../models/operation.model";
import {OperationsRepositoryService} from "../services/operations.repository.service";

@Component({
  selector: 'app-current-accounts',
  templateUrl: './current-accounts.component.html',
  styleUrl: './current-accounts.component.css'
})
export class CurrentAccountsComponent implements OnInit{
  form !: FormGroup;
  type !: string;
  openModal !: boolean;

  constructor(private currentAccountsService : CurrentAccountsRepositoryService,
              private operationsService : OperationsRepositoryService,
              private fb : FormBuilder,
              public appState : AppStateService) {
  }

  searchCurrentAccounts() {
    this.currentAccountsService.searchCurrentAccounts({});
  }

  ngOnInit(): void {
    this.searchCurrentAccounts();
    this.form = this.fb.group({
      accountId : this.fb.control(""),
      firstName : this.fb.control(""),
      lastName : this.fb.control(""),
      amount : this.fb.control(""),
    });
  }

  goToPage(number: number) {
    this.appState.setCurrentAccountsState({currentPage:number});
    this.searchCurrentAccounts();
  }

  changeStatus(id: number, status : string) {
    if(status.includes('ACTIVATED')) {
      this.currentAccountsService.suspendAccount(id).subscribe(() => {
        this.searchCurrentAccounts();
      });
    }else if(status.includes('SUSPENDED')) {
      this.currentAccountsService.activeAccount(id).subscribe(() => {
        this.searchCurrentAccounts();
      });
    }
  }

  closeAccount(id : number) {
    this.currentAccountsService.closeAccount(id).subscribe(() => {
      this.searchCurrentAccounts();
    });
  }

  debitAccount(id : number, firstName : string, lastName : string) {
    this.form.patchValue({
      accountId : id,
      firstName : firstName,
      lastName : lastName
    });
    this.type = "debit";
    this.openModal = true;
  }

  creditAccount(id : number, firstName : string, lastName : string) {
    this.form.patchValue({
      accountId : id,
      firstName : firstName,
      lastName : lastName
    });
    this.type = "credit";
    this.openModal = true;
  }

  closeModal() {
    this.type = "";
    this.openModal = false;
  }

  saveOperation() {
    let operation : Operation = new Operation();
    operation.amount = this.form.value.amount;
    operation.accountId = this.form.value.accountId;
    if(this.type === "debit") {
      this.operationsService.debitAccount(operation).subscribe(() => {
        this.closeModal();
        this.searchCurrentAccounts();
      });
    } else if(this.type === "credit") {
      this.operationsService.creditAccount(operation).subscribe(() => {
        this.closeModal();
        this.searchCurrentAccounts();
      });
    }
    this.form.patchValue({
      amount : ""
    });
  }

}
