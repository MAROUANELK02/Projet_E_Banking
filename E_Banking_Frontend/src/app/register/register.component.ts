import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup} from "@angular/forms";
import {Router} from "@angular/router";
import {AuthRepositoryService} from "../services/auth.repository.service";

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrl: './register.component.css'
})
export class RegisterComponent implements OnInit {
  openClose: boolean = false;
  registerForm!: FormGroup;
  isLoading: boolean = false;
  code!: number;

  constructor(private fb: FormBuilder, private authService: AuthRepositoryService, private router: Router) {
  }

  ngOnInit(): void {
    this.registerForm = this.fb.group({
      email : this.fb.control(''),
      password : this.fb.control(''),
      confirmPassword : this.fb.control(''),
      code : this.fb.control('')
    });
  }

  handleCheckEmail() {
    this.isLoading = true;
    this.authService.checkEmail(this.registerForm.value.email).subscribe(response => {
      this.code = response;
      this.isLoading = false;
      this.openClose = true;
    }, error => {
      this.isLoading = false;
      console.log(error);
    });
  }


  register() {
    if(this.registerForm.value.code !== this.code){
      alert("Invalid code");
      return;
    }
    if(this.registerForm.value.password !== this.registerForm.value.confirmPassword){
      alert("Passwords do not match");
      return;
    }
    this.authService.register(this.registerForm.value.email, this.registerForm.value.password, this.registerForm.value.confirmPassword).subscribe(response => {
      window.alert("Registration successful ! " + "Please login to continue with this username : " + response.username);
      this.router.navigate(['/login']);
      this.openClose = false;
    }, error => {
      this.openClose = false;
      console.log(error);
    });

  }
}
