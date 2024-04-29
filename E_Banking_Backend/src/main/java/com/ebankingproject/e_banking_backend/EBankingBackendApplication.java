package com.ebankingproject.e_banking_backend;

import com.ebankingproject.e_banking_backend.dtos.BankAccountDTO;
import com.ebankingproject.e_banking_backend.dtos.CustomerDTO;
import com.ebankingproject.e_banking_backend.enums.Ville;
import com.ebankingproject.e_banking_backend.exceptions.CustomerNotFoundException;
import com.ebankingproject.e_banking_backend.exceptions.hasAlreadyCurrentAccount;
import com.ebankingproject.e_banking_backend.exceptions.hasAlreadySavingAccount;
import com.ebankingproject.e_banking_backend.services.BankAccountService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

@SpringBootApplication
public class EBankingBackendApplication {
	DecimalFormat decimalFormat = new DecimalFormat("#.00");

	public static void main(String[] args) {
		SpringApplication.run(EBankingBackendApplication.class, args);
	}

	//@Bean
	CommandLineRunner commandLineRunner(BankAccountService bankAccountService) {
		return args -> {

			//customers with bank account
			Stream.of("Hassan","Imane","Mohamed","Ali","Hanane","Nouhaila","Adib","Mouhcine","Farid")
					.forEach(name -> {
						CustomerDTO customerDTO = new CustomerDTO();
						StringBuilder sb = new StringBuilder(name);
						customerDTO.setFirstName(name);
						customerDTO.setLastName(sb.reverse().toString());
						customerDTO.setEmail(name+"@gmail.com");
						int nombreAleatoire = (int) (Math.random() * 5001);
						String codeAleatoire = String.valueOf((char) (65 + Math.random() * 26)) + (char) (65 + Math.random() * 26) + nombreAleatoire;
						customerDTO.setCin(codeAleatoire);
						customerDTO.setVille(Ville.values()[new Random().nextInt(Ville.values().length)]);
						bankAccountService.saveCustomer(customerDTO);
					});

			//random accounts
			bankAccountService.listCustomer().forEach(customer -> {
				try {

					bankAccountService.saveCurrentBankAccount(9000.0, customer.getId());
					bankAccountService.saveSavingBankAccount(5.5, customer.getId());

				} catch (CustomerNotFoundException | hasAlreadyCurrentAccount | hasAlreadySavingAccount e) {
					throw new RuntimeException(e);
				}
			});

			//customers without bank account
			Stream.of("Omar","Yahya","Hajar")
					.forEach(name -> {
						CustomerDTO customerDTO = new CustomerDTO();
						StringBuilder sb = new StringBuilder(name);
						customerDTO.setFirstName(name);
						customerDTO.setLastName(sb.reverse().toString());
						customerDTO.setEmail(name+"@gmail.com");
						int nombreAleatoire = (int) (Math.random() * 5001);
						String codeAleatoire = String.valueOf((char) (65 + Math.random() * 26)) + (char) (65 + Math.random() * 26) + nombreAleatoire;
						customerDTO.setCin(codeAleatoire);
						customerDTO.setVille(Ville.values()[new Random().nextInt(Ville.values().length)]);
						bankAccountService.saveCustomer(customerDTO);
					});

			//random operations
			List<BankAccountDTO> bankAccounts = bankAccountService.bankAccountList();
			for(BankAccountDTO bankAccountDTO:bankAccounts){
				for(int i=0;i<5;i++) {
					double randomCredit = Double.parseDouble(decimalFormat.format(10000 + Math.random() * 120000).replace(',','.'));
					double randomDebit = Double.parseDouble(decimalFormat.format(1000 + Math.random() * 9000).replace(',','.'));

					bankAccountService.credit(bankAccountDTO.getId(), randomCredit, "Versement sur compte");
					bankAccountService.debit(bankAccountDTO.getId(), randomDebit, "Retrait du Compte");
				}
			}

		};
	}

}
