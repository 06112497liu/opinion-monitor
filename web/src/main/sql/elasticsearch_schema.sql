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
          "format": "yyyy-MM-dd HH:mm:ss"
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
        "uuid": {
          "type": "keyword"
        },
        "hot": {
          "type": "integer"
        },
        "hotTime": {
          "type": "date",
          "format": "yyyy-MM-dd HH:mm:ss"
        }
      }
    }
  }
}

DELETE /bbd_opinion_op_record
PUT bbd_opinion_op_record
{
  "settings": {
    "number_of_replicas": 0,
    "number_of_shards": 1
  },
  "mappings": {
    "opinion_op_record": {
        "properties": {
           "opTime": {
              "type": "date",
              "format": "yyyy-MM-dd HH:mm:ss"
           },
           "opType": {
              "type": "integer"
           },
           "operator": {
              "type": "keyword"
           },
           "removeContent": {
              "type": "text",
              "fields": {
                 "keyword": {
                    "type": "keyword",
                    "ignore_above": 256
                 }
              }
           },
           "removeNote": {
              "type": "text",
              "fields": {
                 "keyword": {
                    "type": "keyword",
                    "ignore_above": 256
                 }
              }
           },
           "removeReason": {
              "type": "integer"
           },
           "targeter": {
              "type": "keyword"
           },
           "transferContent": {
              "type": "text",
              "fields": {
                 "keyword": {
                    "type": "keyword",
                    "ignore_above": 256
                 }
              }
           },
           "transferNote": {
              "type": "text",
              "fields": {
                 "keyword": {
                    "type": "keyword",
                    "ignore_above": 256
                 }
              }
           },
           "transferType": {
              "type": "integer"
           },
           "uuid": {
              "type": "keyword"
           }
        }
    }
  }
 }


DELETE /bbd_opinion_event_record
PUT bbd_opinion_op_record
{
  "settings": {
    "number_of_replicas": 0,
    "number_of_shards": 1
  },
  "mappings": {
    "opinion_event_record": {
        "properties": {
           "opinionId": {
              "type": "keyword"
           },
           "eventId": {
              "type": "long"
           },
           "matchTime": {
              "type": "date",
              "format": "yyyy-MM-dd HH:mm:ss"
           }
        }
    }
  }
 }