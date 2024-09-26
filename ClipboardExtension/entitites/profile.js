import { NationalId } from "./national-id.js";
import { Document } from "./document.js"

export class Profile {
  constructor(data) {
    if (!data) {
      this.id = null;
      this.name = null;
      this.nationalIds = [];
      this.documents = [];
      return;
    }
    this.id = data.id;
    this.name = data.name;
    this.nationalIds = data.nationalIds.map(id => new NationalId(id));
    this.documents = data.documents.map(doc => new Document(doc));
  }

  static profileList = [];

  static setProfileList(profiles) {
    for (let i = 0; i < profiles.length; i++) {
      this.profileList.push(new Profile(profiles[i]));
    }
  }
}