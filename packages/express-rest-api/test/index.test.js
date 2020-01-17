const request = require('supertest');
const app = require('../server/app.js');

describe('GET /', function() {
  it('return json response', function() {
    return request(app)
      .get('/')
      .expect(500)
      .expect('Content-Type',/json/)
      .expect('{"text":"some json"}')
  })
})
