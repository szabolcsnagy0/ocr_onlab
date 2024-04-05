class Person {
  constructor(data) {
    console.log(data);
    if (!data) {
        this.name = null;
      return;
    }
    this.name = data.name;
  }

  setData(data) {
    this.name = data.name;
  }
}

const person = new Person();

export { person, Person };