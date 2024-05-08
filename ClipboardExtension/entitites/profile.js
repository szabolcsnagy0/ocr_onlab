import { NationalId } from "./national-id.js";

export class Profile {
  constructor(data) {
    console.log(data);
    if (!data) {
      this.id = null;
      this.name = null;
      this.nationalIds = [];
      return;
    }
    this.id = data.id;
    this.name = data.name;
    this.nationalIds = data.nationalIds.map(id => new NationalId(id));
  }

  static profileList = [];

  static setProfileList(profiles) {
    for (let i = 0; i < profiles.length; i++) {
      this.profileList.push(new Profile(profiles[i]));
    }
  }
}