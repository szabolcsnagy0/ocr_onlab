export class Document {
    constructor(data) {
        if (!data) {
            this.id = null;
            this.name = null;
            this.fieldsList = null;
            return;
        }
        this.id = data.id;
        this.name = data.documentName;
        this.fieldsList = data.fieldsList;
    }
}