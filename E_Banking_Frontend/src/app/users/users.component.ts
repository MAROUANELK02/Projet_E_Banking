import {Component, OnInit} from '@angular/core';
import {CustomersRepositoryService} from "../services/customers.repository.service";
import {AppStateService} from "../services/app-state.service";

@Component({
  selector: 'app-users',
  templateUrl: './users.component.html',
  styleUrl: './users.component.css'
})
export class UsersComponent implements OnInit {
  query : string = "";

  constructor(private customersService : CustomersRepositoryService,
              public appState : AppStateService) {
  }

  searchCustomers() {
    this.customersService.searchCustomers({});
  }

  handleAccount(id : number) {

  }

  ngOnInit(): void {
    this.searchCustomers();
  }

  handleEdit(id : number) {

  }

  handleDelete(id : number) {

  }

  goToPage(number: number) {
    this.appState.setUsersState({currentPage:number});
    this.searchCustomers();
  }

  handleSearch() {
    this.customersService.searchCustomers({keyword:this.query, currentPage :0});
  }
}
