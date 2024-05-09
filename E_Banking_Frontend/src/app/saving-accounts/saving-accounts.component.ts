import { Component } from '@angular/core';
import {AppStateService} from "../services/app-state.service";
import {SavingAccountsRepositoryService} from "../services/saving-accounts.repository.service";
import {FormBuilder, FormGroup} from "@angular/forms";
import {OperationsRepositoryService} from "../services/operations.repository.service";
import {Operation} from "../models/operation.model";

@Component({
  selector: 'app-saving-accounts',
  templateUrl: './saving-accounts.component.html',
  styleUrl: './saving-accounts.component.css'
})
export class SavingAccountsComponent {
  openModal!: boolean;
  type!: string;
  form!: FormGroup;

  constructor(private savingAccountsService : SavingAccountsRepositoryService,
              private operationsService : OperationsRepositoryService,
              private fb : FormBuilder,
              public appState : AppStateService) {
  }

  searchSavingAccounts() {
    this.savingAccountsService.searchSavingAccounts({});
  }

  ngOnInit(): void {
    this.searchSavingAccounts();
    this.form = this.fb.group({
      accountId : this.fb.control(""),
      firstName : this.fb.control(""),
      lastName : this.fb.control(""),
      amount : this.fb.control(""),
    });
  }

  goToPage(number: number) {
    this.appState.setSavingAccountsState({currentPage:number});
    this.searchSavingAccounts();
  }

  changeStatus(id: number, status: string) {
    if(status.includes('ACTIVATED')) {
      this.savingAccountsService.suspendAccount(id).subscribe(() => {
        this.searchSavingAccounts();
      });
    }else if(status.includes('SUSPENDED')) {
      this.savingAccountsService.activeAccount(id).subscribe(() => {
        this.searchSavingAccounts();
      });
    }
  }

  closeAccount(id : number) {
    this.savingAccountsService.closeAccount(id).subscribe(() => {
      this.searchSavingAccounts();
    });
  }

  debitAccount(id: number, firstName: any, lastName: any) {
    this.form.patchValue({
      accountId : id,
      firstName : firstName,
      lastName : lastName
    });
    this.type = "debit";
    this.openModal = true;
  }

  creditAccount(id: number, firstName: any, lastName: any) {
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
        this.searchSavingAccounts();
      });
    } else if(this.type === "credit") {
      this.operationsService.creditAccount(operation).subscribe(() => {
        this.closeModal();
        this.searchSavingAccounts();
      });
    }
    this.form.patchValue({
      amount : ""
    });
    this.closeModal();
  }
}
