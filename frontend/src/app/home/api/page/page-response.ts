export interface PageResponse<T>{
  content: T[],
  first: boolean,
  last: boolean
}
