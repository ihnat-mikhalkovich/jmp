db.tasks.aggregate([
    {
        "$addFields": {
            "isoDeadline": {
                "$toDate": "$deadline"
            }
        }
    },
    {
        "$match" : {
            "isoDeadline" : {
                "$lt" : new Date("2021-06-17T18:00:00Z")
            }
        }
    },
    {
        "$project" : {
            "_id" : 1,
            "dateOfCreation" : 1,
            "deadline" : 1,
            "name" : 1,
            "description" : 1,
            "subtasks" : 1,
            "category" : 1
        }
    }
])