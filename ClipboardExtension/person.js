export class Person {
  constructor(data) {
    console.log(data);
    if (!data) {
      this.name = null;
      this.document_nr = null;
      this.date_of_birth = null;
      return;
    }
    this.name = data.name;
    this.document_nr = data.documentNr;
    this.date_of_birth = data.dateOfBirth;
  }

  setData(data) {
    this.name = data.name;
    this.document_nr = data.documentNr;
    this.date_of_birth = data.dateOfBirth;
  }
}

const person = new Person();

export { person };