import { BookSimpleResponse } from '../../../book/dto/response/book-simple-response';

export interface CustomerResponse {
  id: number;
  name: string;
  bookSimpleResponses: BookSimpleResponse[];
}
