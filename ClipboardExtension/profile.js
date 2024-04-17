import { NationalId } from "./national-id.js";

export class Profile {
  constructor(data) {
    console.log(data);
    if (!data) {
      this.id = null;
      this.name = null;
      this.nationalId = null;
      return;
    }
    this.id = data.id;
    this.name = data.name;
    this.nationalId = new NationalId(data.nationalId);
  }

  static profileList = [];

  static setProfileList(profiles) {
    for (let i = 0; i < profiles.length; i++) {
      this.profileList.push(new Profile(profiles[i]));
    }
  }
}