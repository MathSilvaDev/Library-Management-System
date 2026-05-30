import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { BookService } from '../../api/book/service/book.service';
import { EditBookRequest } from '../../api/book/dto/request/edit-book-request';
import { PublisherService } from '../../api/publisher/service/publisher.service';
import { PublisherResponse } from '../../api/publisher/dto/publisher-response';
import { FormsModule } from '@angular/forms';

type PageContent = { first: boolean | null; last: boolean | null };

@Component({
  selector: 'app-book-edit',
  imports: [ FormsModule ],
  templateUrl: './book-edit.html',
  styleUrl: './book-edit.scss',
})
export class BookEdit {

  id!: number;

  name: string = '';
  quantity: number | null = null;
  publishedIn: string = '';
  publisherId!: number

  publisherName: string = '';
  searchedPublisherName: string = '';
  publishers: PublisherResponse[] = [];
  selectedPublisher: PublisherResponse | null = null;
  publisherPage: number = 0;
  publisherPageContent: PageContent = { first: null, last: null };

  get selectablePublishers(): PublisherResponse[] {
    if (!this.selectedPublisher) {
      return this.publishers;
    }

    return this.publishers.filter((publisher) => publisher.id !== this.selectedPublisher?.id);
  }

  constructor(
    private route: ActivatedRoute,
    public router: Router,
    private bookService: BookService,
    private publisherService: PublisherService
  ){}

  ngOnInit(){
    this.id = Number(this.route.snapshot.paramMap.get('id'));
    this.findById(this.id);
    this.findAllPublishersByName();
  }

  findById(id: number){

    this.bookService.findById(id).subscribe({
      next: (response) => {
        this.name = response.name;
        this.quantity = response.quantity;
        this.publishedIn = this.formatLocalDate(response.publishedIn);
        this.publisherId = response.publisherId;
        this.selectedPublisher = {
          id: response.publisherId,
          name: response.publisherName
        };
        this.findSelectedPublisherById(response.publisherId);
        this.showSelectedPublisherFirst();
      },
      error(err) {
        console.log('ERROR: ' + err)
      },
    })
  }

  saveEdit(){
    const request: EditBookRequest = {
      name: this.name.trim(),
      quantity: this.quantity!,
      publishedIn: this.publishedIn as unknown as Date,
      publisherId: this.publisherId
    }

    this.bookService.editInfo(this.id, request).subscribe({
      next: () => {
        this.router.navigate(['/']);
        window.alert("Saved Successfully");
        
      },
      error: (err) => {
        console.log('ERROR: ' + err)
      }
    });
  }

  //publisher
  searchPublishers(){
    this.searchedPublisherName = this.publisherName.trim();
    this.publisherPage = 0;
    this.findAllPublishersByName();
  }

  findAllPublishersByName(value: number = 0){
    const publisherName = this.searchedPublisherName;

    this.publisherPage += this.changePage(value, this.publisherPageContent.first, this.publisherPageContent.last);

    this.publisherService.findAllByName(publisherName, 'ALL', this.publisherPage).subscribe({
      next: (response) => {
        this.publisherPageContent = { first: response.first, last: response.last };
        this.publishers = response.content;
        this.showSelectedPublisherFirst();
      },
      error: () => {
        console.log("error: findAllPublisherByName");
      }
    });
  }

  selectPublisher(publisher: PublisherResponse) {
    this.selectedPublisher = publisher;
    this.publisherId = publisher.id;
    this.showSelectedPublisherFirst();
  }

  private findSelectedPublisherById(id: number) {
    this.publisherService.findById(id).subscribe({
      next: (response) => {
        this.selectedPublisher = response;
        this.publisherId = response.id;
        this.showSelectedPublisherFirst();
      },
      error: () => {
        console.log("error: findSelectedPublisherById");
      }
    });
  }

  private formatLocalDate(value: Date | string): string {
    if (typeof value === 'string') {
      return value.slice(0, 10);
    }

    const year = value.getFullYear();
    const month = String(value.getMonth() + 1).padStart(2, '0');
    const day = String(value.getDate()).padStart(2, '0');

    return `${year}-${month}-${day}`;
  }

  private changePage(value: number, first: boolean | null, last: boolean | null): number {
    if (first === null || last === null) return 0;
    if (first && value < 0) return 0;
    if (last && value > 0) return 0;

    return value;
  }

  private showSelectedPublisherFirst() {
    if (this.selectedPublisher) {
      this.publishers = [
        this.selectedPublisher,
        ...this.publishers.filter((publisher) => publisher.id !== this.selectedPublisher?.id)
      ];
    }
  }

}
