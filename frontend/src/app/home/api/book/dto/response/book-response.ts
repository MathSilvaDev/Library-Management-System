import { CustomerSimpleResponse } from '../../../customer/dto/response/customer-simple-response';

export interface BookResponse {
  id: number;
  name: string;
  quantity: number;
  borrowedQuantity: number;
  publisherName: string;
  publishedIn: Date;
  publisherId: number;
  customerSimpleResponses: CustomerSimpleResponse[];
}
