import _ from 'lodash';
import axios from 'axios';
let config = require('../../config');

let backendConfigurer = function () {
  switch (process.env.NODE_ENV) {
    case 'testing':
    case 'development':
      return 'http://' + config.dev.backendHost + ':' + config.dev.backendPort;
    case 'production':
      return 'https://' + config.build.backendHost + ':' + config.build.backendPort;
  }
}

let backendUrl = backendConfigurer();

let AXIOS = axios.create({
  baseURL: backendUrl
  // headers: {'Access-Control-Allow-Origin': frontendUrl}
});

export default {
  name: 'eventregistration',
  data() {
    return {
      persons: [],
      volunteers: [],
      events: [],
      cinemas: [],
      registrations: [],
      bitcoins: [],
      newPerson: '',
      personType: '',
      newEvent: {
        name: '',
        date: '2017-12-08',
        startTime: '09:00',
        endTime: '11:00'
      },
      selectedPerson: '',
      selectedVolunteer: '',
      selectedEvent: '',
      userID: '',
      amount: 0,
      errorPerson: '',
      errorVolunteer: '',
      errorEvent: '',
      errorCinema: '',
      errorRegistration: '',
      response: [],
    }
  },
  created: function () {
    // Initializing persons
    AXIOS.get('/persons')
    .then(response => {
      this.persons = response.data;
      this.persons.forEach(person => this.getRegistrations(person.name))
    })
    .catch(e => {this.errorPerson = e});

    AXIOS.get('/events').then(response => {this.events = response.data}).catch(e => {this.errorEvent = e});

  },

  methods: {

    createPerson: function (personType, personName) {
      if (personType === "Person") {
       AXIOS.post('/persons/'.concat(personName), {}, {})
        .then(response => {
          this.persons.push(response.data);
          this.errorPerson = '';
          this.newPerson = '';
       })
       .catch(e => {
          e = e.response.data.message ? e.response.data.message : e;
          this.errorPerson = e;
         console.log(e);
        });
      }
      else if (personType === "Volunteer"){
        AXIOS.post('/volunteers/'.concat(personName), {}, {})
        .then(response => {
          this.volunteers.push(response.data);
          this.persons.push(response.data);
          this.errorVolunteer = '';
          this.newPerson = '';
       })
       .catch(e => {
          e = e.response.data.message ? e.response.data.message : e;
          this.errorVolunteer = e;
         console.log(e);
        });
      }
    },

    createEvent: function (newEvent) {
      if (newEvent.movie === undefined || newEvent.movie === null || newEvent.movie.trim().length === 0) {
       AXIOS.post('/events/'.concat(newEvent.name), {}, {params: newEvent})
       .then(response => {
         this.events.push(response.data);
         this.errorEvent = '';
         this.newEvent.name = this.newEvent.make = this.newEvent.movie = this.newEvent.company = this.newEvent.artist = this.newEvent.title = '';
       })
       .catch(e => {
         e = e.response.data.message ? e.response.data.message : e;
          this.errorEvent = e;
          console.log(e);
        });
      }
      else {
        AXIOS.post('/cinemas/'.concat(newEvent.name), {}, {params: newEvent})
        .then(response => {
          this.cinemas.push(response.data);
          this.events.push(response.data);
          this.errorCinema = '';
          this.newEvent.name = this.newEvent.make = this.newEvent.movie = this.newEvent.company = this.newEvent.artist = this.newEvent.title = '';
        })
        .catch(e => {
          e = e.response.data.message ? e.response.data.message : e;
           this.errorCinema = e;
           console.log(e);
         });
      }
    },

    registerEvent: function (personName, eventName) {
      let event = this.events.find(x => x.name === eventName);
      let person = this.persons.find(x => x.name === personName);
      let params = {
        person: person.name,
        event: event.name
      };

      AXIOS.post('/register', {}, {params: params})
      .then(response => {
        this.registrations.push(response.data);
        person.eventsAttended.push(event);
        this.selectedPerson = '';
        this.selectedEvent = '';
        this.errorRegistration = '';
      })
      .catch(e => {
        e = e.response.data.message ? e.response.data.message : e;
        this.errorRegistration = e;
        console.log(e);
      });
    },

    assignEvent: function (volunteerName, eventName) {
      let event = this.events.find(x => x.name === eventName);
      let volunteer = this.volunteers.find(x => x.name === volunteerName);
      let params = {
        volunteer: volunteer.name,
        event: event.name
      };

      AXIOS.post('/assign', {}, {params: params})
      .then(response => {
        volunteer.eventsVolunteered.push(response.data);
        this.selectedVolunteer = '';
        this.selectedEvent = '';
        this.errorAssignment = '';
      })
      .catch(e => {
        e = e.response.data.message ? e.response.data.message : e;
        this.errorAssignment = e;
        console.log(e);
      });
    },

    makePayment: function (personName, eventName, userID, amount) {
      let event = this.events.find(x => x.name === eventName);
      let person = this.persons.find(x => x.name === personName);
      let registration = this.registrations.find(x => (x.person === person) || (x.event === event));
      let params = {
        person: person.name,
        event: event.name,
        registration,
        userID,
        amount
      };

      AXIOS.post('/bitcoins', {}, {params: params})
      .then(response => {
        this.bitcoins.push(response.data);
        this.registration.bitcoin = response.data;
        this.selectedPerson = '';
        this.selectedEvent = '';
        this.userID = '';
        this.amount = 0;
        this.errorPayment = '';
      })
      .catch(e => {
        e = e.response.data.message ? e.response.data.message : e;
        this.errorPayment = e;
        console.log(e);
      });
    },

    getRegistrations: function (personName) {
      AXIOS.get('/events/person/'.concat(personName))
      .then(response => {
        if (!response.data || response.data.length <= 0) return;

        let indexPart = this.persons.map(x => x.name).indexOf(personName);
        this.persons[indexPart].eventsAttended = [];
        response.data.forEach(event => {
          this.persons[indexPart].eventsAttended.push(event);
        });
      })
      .catch(e => {
        e = e.response.data.message ? e.response.data.message : e;
        console.log(e);
      });
    },

    getVolunteeredEvents: function (volunteerName) {
      AXIOS.get('/events/volunteer/'.concat(volunteerName))
      .then(response => {
        if (!response.data || response.data.length <= 0) return;

        let indexPart = this.volunteers.map(x => x.name).indexOf(volunteerName);
        this.volunteers[indexPart].eventsVolunteered = [];
        response.data.forEach(event => {
          this.volunteers[indexPart].eventsVolunteered.push(event);
        });
      })
      .catch(e => {
        e = e.response.data.message ? e.response.data.message : e;
        console.log(e);
      });
    },

    getBitcoin: function (registrationId) {
      AXIOS.get('/registrations'.concat(registrationId).concat('bitcoin'))
      .then(response => {
        if (!response.data || response.data.length <= 0) return;

        let indexPart = this.registrations.map(x => x.id).indexOf(registrationId);
        this.registrations[indexPart].bitcoin = response.data;
      })
      .catch(e => {
        e = e.response.data.message ? e.response.data.message : e;
        console.log(e);
      });
    },
  }
}
