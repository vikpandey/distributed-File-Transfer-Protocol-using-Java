/**
 * Created by vikas on 9/15/16.
 */

var express =  require('express');
var router = express.Router();
var assert = require('assert');

        var value;
        router.post('/', function (req, res, next) {

            console.log('in the post method');

            console.log(req.body);

                var collection1 = db.collection('KeywordCategoryCollection');


            var searchQuery =  req.param('keyword', null);
            console.log(searchQuery);

            var query = {"keyword": {'$regex':searchQuery,$options:'i'}};
            console.log(query);
            var projection = {"_id":0, "category":1};
            var cursor = collection1.find(query);
            cursor.project(projection);
            console.log('before doc cursor ');

            cursor.forEach( function (doc) {

                console.log(doc.category);
                console.log(doc);
               // res.send(doc);
                value = doc.category;
                console.log('value of: ',value);

                searchKeywordCollection(value);

            },
                function (err) {

                    assert.equal(err, null);
                   return db.close();
                }
            );

            function searchKeywordCollection (value) {

                var collection2 = db.collection(value);
                var query2 = {"keywords": {'$regex':searchQuery,$options:'i'}};
                console.log(query2);
                var projection2 = {"_id":0};
                var cursor2 = collection2.find(query2);
                cursor2.project(projection2);

                cursor2.forEach( function (doc2) {

                   // console.log(doc2.keywords);
                    console.log(doc2);
                    res.end();
                   // res.send(doc2);
                },
                    function (err) {
                        assert.equal(err, null);
                        db.close();
                    }
                );

            }
        });

module.exports = router;