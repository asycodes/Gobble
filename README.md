# Gobble - Team 53

## Team members
- Adelaine Ruth Hanako Suhendro, 1007059
- Avitra Phon, 1006946
- Muvil Kothari, 1006885
- Khoo Yong Xuan, 1006962
- Raphael Xujie Yip, 1006657
- Muhammad Asyraf Bin Omar, 1006938

## About Gobble

"Gobble" is a centralized review app that aggregates data from diverse sources like Google Business, SmartLocal reviews, TikTok, and user-generated content. Gobble stands out by offering personalized restaurant recommendations based on user profiles, incorporating generative AI to suggest specific dishes within budget constraints, and enabling comparative insights between eateries.

API documentation utilized: Google Business, Yelp, Tiktok, OpenAI

- View the Figma Prototype of Gobble [here](https://www.figma.com/proto/kKKdI45mlW6sPdlUEojrtE/50.001?type=design&node-id=48-151&t=EhO0q2kSAlFj3sfv-0&scaling=scale-down&page-id=0%3A1&starting-point-node-id=39%3A49)
- View Gobble's business and technical proposal(Checkoff 1) [here](https://github.com/ilenhanako/t4app/files/14941705/1D.project.gobble.pdf)
- View Gobble's poster [here](https://github.com/ilenhanako/t4app/assets/9971306/3c243634-6655-4215-bd54-13a0f1be412b)
- View Gobble's technical implementation(Checkoff 2) [here](https://github.com/ilenhanako/t4app/files/15059838/1D.project.gobble.1.pdf)
- View Gobble's Launch Video [here]()

## Gobble Poster
![Gobble Poster](https://github.com/ilenhanako/t4app/assets/9971306/043d2807-228f-4d66-bce6-848a7eea5ad4)

## OOP Principles Used
**1. Encapsulation**
- Classes such as Restaurant and UserProfile encapsulate data and its manipulation, exposing only the necessary information through their public methods.
  
**2. Abstraction**
- Interfaces like TripAdvisorService and YelpService abstract the details of HTTP requests and responses, providing a clear API for data retrieval.
  
**3. Inheritance**
- By extending from base classes such as Fragment and ViewModel, specialized behaviors are enabled (e.g., HomeFragment, HomeFragmentViewModel) while reusing common functionality.
  
**4. Polymorphism**
- Overridden methods within Activities, Fragments, and ViewModels allow for polymorphic behavior that is tailored to specific needs.
