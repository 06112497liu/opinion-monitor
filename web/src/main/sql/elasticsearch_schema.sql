DELETE bbd_opinion
PUT bbd_opinion
{
  "settings": {
    "number_of_replicas": 0,
    "number_of_shards": 1
  },
  "mappings": {
    "opinion": {
      "properties": {
        "uuid": {
          "type": "keyword"
        },
        "titile": {
          "type": "text"
        },
        "summary": {
          "type": "text"
        },
        "content": {
          "type": "text"
        },
        "source": {
          "type": "keyword"
        },
        "link": {
          "type": "keyword"
        },
        "mediaType": {
          "type": "integer"
        },
        "website": {
          "type": "keyword"
        },
        "publishTime": {
          "type": "date",
          "format": ["yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss"]
        },
        "hot": {
          "type": "integer"
        },
        "emotion": {
          "type": "integer"
        },
        "keyword": {
          "type": "keyword"
        },
        "keys": {
          "type": "keyword"
        },
        "similiarCount": {
          "type": "integer"
        },
        "commentCount": {
          "type": "integer"
        },
        "events": {
          "type": "long"
        },
        "opStatus": {
          "type": "integer"
        },
        "opOwner":{
          "type": "long"
        },
        "operators":{
          "type": "long"
        },
        "transferType": {
          "type": "integer"
        },
        "firstWarnTime": {
          "type": "date",
          "format": ["yyyy-MM-dd HH:mm:ss"]
        }
      }
    }
  }
}

DELETE /bbd_opinion_hot
PUT /bbd_opinion_hot
{
  "settings": {
    "number_of_replicas": 0,
    "number_of_shards": 1
  },
  "mappings": {
    "hot": {
      "properties": {
        "id": {
          "type": "keyword"
        },
        "uuid": {
          "type": "keyword"
        },
        "hot": {
          "type": "integer"
        }
      }
    }
  }
}