import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { CustomerService } from '../../api/customer/service/customer.service';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-customer-edit',
  imports: [ FormsModule ],
  templateUrl: './customer-edit.html',
  styleUrl: './customer-edit.scss',
})
export class CustomerEdit {

  id!: number;

  name: string = '';

  constructor(
    private route: ActivatedRoute,
    public router: Router,
    private customerService: CustomerService
  ){}

  ngOnInit(){
    this.id = Number(this.route.snapshot.paramMap.get('id'));
    this.findById(this.id);
    
  }

  findById(id: number){
    this.customerService.findById(id).subscribe({
      next: (response) => {
        this.name = response.name;
      },
      error(err) {
        console.log('ERROR: ' + err)
      },
    })
  }

  saveEdit(){
    this.customerService.editInfo(this.id, this.name.trim()).subscribe({
      next: () => {
        this.router.navigate(['/']);
        window.alert("Saved Successfully");
        
      },
      error: (err) => {
        console.log('ERROR: ' + err)
      }
    });
  }
}
