export class NationalId {
    constructor(data) {
        if (!data) {
            this.name = null;
            this.sex = null;
            this.nationality = null;
            this.dateOfBirth = null;
            this.dateOfExpiry = null;
            this.documentNr = null;
            this.can = null;
            this.placeOfBirth = null;
            this.nameAtBirth = null;
            this.mothersName = null;
            this.authority = null;
            return;
        }
        this.name = data.name;
        this.sex = data.sex;
        this.nationality = data.nationality;
        this.dateOfBirth = data.dateOfBirth;
        this.dateOfExpiry = data.dateOfExpiry;
        this.documentNr = data.documentNr;
        this.can = data.can;
        this.placeOfBirth = data.placeOfBirth;
        this.nameAtBirth = data.nameAtBirth;
        this.mothersName = data.mothersName;
        this.authority = data.authority;
    }
}