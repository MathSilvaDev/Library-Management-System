import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { PublisherService } from '../../api/publisher/service/publisher.service';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-publisher-edit',
  imports: [ FormsModule ],
  templateUrl: './publisher-edit.html',
  styleUrl: './publisher-edit.scss',
})
export class PublisherEdit {

  id!: number;

  name: string = '';

  constructor(
    private route: ActivatedRoute,
    public router: Router,
    private publisherService: PublisherService
  ){}

  ngOnInit(){
    this.id = Number(this.route.snapshot.paramMap.get('id'));
    this.findById(this.id);
    
  }

  findById(id: number){
    this.publisherService.findById(id).subscribe({
      next: (response) => {
        this.name = response.name;
      },
      error(err) {
        console.log('ERROR: ' + err)
      },
    })
  }

  saveEdit(){
    this.publisherService.editInfo(this.id, this.name.trim()).subscribe({
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
