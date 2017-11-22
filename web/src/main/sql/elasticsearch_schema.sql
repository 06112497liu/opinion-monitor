# 舆情
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
          "format": "yyyy-MM-dd||yyyy-MM-dd HH:mm:ss"
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
        "warnTime": {
          "properties": {
            "firstWarnTime": {
              "type": "date",
              "format": "yyyy-MM-dd HH:mm:ss"
            },
            "firstWarnTimeOne": {
              "type": "date",
              "format": "yyyy-MM-dd HH:mm:ss"
            },
            "firstWarnTimeTwo": {
              "type": "date",
              "format": "yyyy-MM-dd HH:mm:ss"
            },
            "firstWarnTimeThree": {
              "type": "date",
              "format": "yyyy-MM-dd HH:mm:ss"
            }
          }
        }
      }
    }
  }
}

# 舆情热度
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

# 舆情操作记录
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


# 舆情事件记录
DELETE /bbd_opinion_event_record
PUT bbd_opinion_event_record
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
           },
           "matchTimeTrim": {
              "type": "date",
              "format": "yyyy-MM-dd HH:mm:ss"
           }
        }
    },
    "opinion_event_hot_record": {
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
           },
           "matchTimeTrim": {
              "type": "date",
              "format": "yyyy-MM-dd HH:mm:ss"
           }
        }
    },
    "opinion_event_warn_record": {
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
           },
           "matchTimeTrim": {
              "type": "date",
              "format": "yyyy-MM-dd HH:mm:ss"
           }
        }
    }
  }
 }

# 相同文章信息索引
DELETE /bbd_article
PUT /bbd_article
{
  "settings": {
    "number_of_replicas": 0,
    "number_of_shards": 1
  },
  "mappings": {
    "article": {
      "properties": {
        "opinion_id": {
          "type": "keyword"
        },
        "hot": {
          "type": "integer"
        },
        "id": {
          "type": "keyword"
        },
        "title": {
          "type": "text"
        },
        "source": {
          "type": "keyword"
        },
        "link": {
          "type": "keyword"
        }
      }
    }
  }
}