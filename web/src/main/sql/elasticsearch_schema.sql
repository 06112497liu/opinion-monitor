PUT bbd_opinion
{
  "settings": {
    "index": {
      "number_of_shards": 1,
      "number_of_replicas": 1
    }
  },
  "mappings": {
    "opinion": {
      "properties": {
        "esId": {
          "type": "keyword"
        },
        "source": {
          "type": "keyword"
        },
        "uuid": {
          "type": "keyword"
        },
        "startTime": {
          "type": "date",
          "format": "yyyy-MM-dd HH:mm:ss"
        },
        "gmtCreate": {
          "type": "date",
          "format": "yyyy-MM-dd HH:mm:ss"
        },
        "gmtModified": {
          "type": "date",
          "format": "yyyy-MM-dd HH:mm:ss"
        }
      }
    }
  }
}
