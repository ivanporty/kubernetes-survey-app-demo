/*
 * Copyright 2019 Google LLC. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package vote;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@RestController
public class VoteController {
    private Logger logger = LoggerFactory.getLogger(getClass());

    private final Map<String, Integer> votes = new HashMap<>();

    @RequestMapping(value = "/", method = RequestMethod.POST, consumes =
            MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<String> vote(
            @RequestBody Map<String, Integer> newVotes) {
        logger.info("Got new votes: {}", newVotes);

        for (Map.Entry<String, Integer> vote : newVotes.entrySet()) {
            int count = 1;
            if (votes.containsKey(vote.getKey())) {
                count += votes.get(vote.getKey());
            }
            votes.put(vote.getKey(), count);
        }

        logger.info("Notifying new votes via notification-service...");

        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<Map<String, Integer>> entity = new HttpEntity<>(newVotes);
        ResponseEntity<String> notificationResponse =
                restTemplate.exchange(
                        "http://notification-service/notify",
                        HttpMethod.POST,
                        entity,
                        String.class);

        logger.info("Current votes: {}", newVotes);

        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/debugVote")
    public String debugVote() {
        return "Votes: " + votes.toString();
    }
}
