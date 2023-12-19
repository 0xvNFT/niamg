function callTTTrack(){
    console.log("call fb track event ----");
    try {
        // fbq('track', 'Purchase', {value: 1, currency: 'USD'});
        ttq.track('CompleteRegistration');
    }catch (e){
        console.log("fb error = ",e);
    }
}
function callFbTrack(){
    console.log("call fb track event ----");
    try {
        fbq('track', 'Purchase', {value: 1, currency: 'USD'});
    }catch (e){
        console.log("fb error = ",e);
    }
}
function callTTPayTrack(){
    console.log("call tt pay complete track event ----");
    try {
        ttq.track('CompletePayment')
    }catch (e){
        console.log("tt pay error = ",e);
    }
}

