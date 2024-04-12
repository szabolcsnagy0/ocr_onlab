import { fetchData } from "./api.js";
import { person } from "./person.js";

setInterval(function() {
    fetchData()
        .then(data => {
            person.setData(data);
        })
        .catch(error => {
            // console.error(error);
        });
}, 100);