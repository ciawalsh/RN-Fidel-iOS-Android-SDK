//
//  CalanderManager.m
//  fidelSpike2
//
//  Created by Charles Walsh on 26/04/2019.
//  Copyright © 2019 Facebook. All rights reserved.
//

#import "CalanderManager.h"
#import <React/RCTLog.h>
#import "AppDelegate.h"
#import <Fidel/Fidel-Swift.h>

@implementation CalanderManager
{
  NSObject *_rootView;
}

// To export a module named above
RCT_EXPORT_MODULE();

// Export a method that sends metadata to fidel
RCT_EXPORT_METHOD(setMetaData:(NSString *)userID){
  NSDictionary *userId = @{@"userID": userID};
  [FLFidel setMetaData:userId];
}

// Create a function called "present" that has a callback for both success & error.
RCT_REMAP_METHOD(present,
                 presentWithResolver:(RCTPromiseResolveBlock)resolve
                 rejecter:(RCTPromiseRejectBlock)reject)
{
  // Set the delgate variable to get access to the rootViewController to be able to Pop the Fidel modal on top of RN.
  AppDelegate *delegate = (AppDelegate *)[[UIApplication sharedApplication] delegate];
  // Call the Fidel "present" function
  [FLFidel present:delegate.rootViewController onCardLinkedCallback:^(FLLinkResult * _Nonnull result) {
    // Extract variables from the FLLinkResult callback
    NSString *resultID = [result id];
    NSString *resultcreated = [result created];
    NSString *resultupdated = [result updated];
    NSString *resulttype = [result type];
    NSString *resultscheme = [result scheme];
    NSString *resultprogramID = [result programId];
    BOOL resultmapped = [result mapped];
    BOOL resultlive = [result live];
    NSString *resultlastnumbers = [result lastNumbers];
    NSInteger resultexpyear = [result expYear];
    NSInteger resultexpmonth = [result expMonth];
    NSString *resultcountryCode = [result countryCode];
    NSString *resultaccountId = [result accountId];
    NSDictionary *resultmetaData = [result metaData];
    
//    Can uncomment below to log in the Xcode terminal the results of the above...
//    NSLog(@"Success resultID, created, updated, type, scheme, programID, mapped, live, lastNumbers, expYear, expMonth, countryCode, accountID, metaData, %@, %@, %@, %@, %@, %@, %d, %d, %@, %ld, %ld, %@, %@, %@", resultID, resultcreated, resultupdated, resulttype, resultscheme, resultprogramID, resultmapped, resultlive, resultlastnumbers, (long)resultexpyear, (long)resultexpmonth, resultcountryCode, resultaccountId, resultmetaData);
    
    // Create an object with the above extracted variables
    NSDictionary * fidelResult = [NSDictionary dictionaryWithObjectsAndKeys:
      resultID,@"ID",
      resultcreated,@"created",
      resultupdated,@"updated",
      resulttype,@"type",
      resultscheme,@"scheme",
      resultprogramID,@"programID",
      @(resultmapped),@"mapped",
      @(resultlive),@"live",
      resultlastnumbers,@"lastfour",
      @(resultexpyear),@"expYear",
      @(resultexpmonth),@"expMonth",
      resultcountryCode,@"countryCode",
      resultaccountId,@"accountID",
      resultmetaData,@"metaData",
    nil];
    // Resolve the promise with the fidelResult object.
    resolve(fidelResult);
  } onCardLinkFailedCallback:^(FLLinkError * _Nonnull error) {
    // Extract the info from the FLLinkError error callback
    NSString *errorDate = [error date];
    NSString *errorMessage = [error message];
    NSString *errorCode = [error code];
    // Create an object with the above variables
    NSDictionary * fidelError = [NSDictionary dictionaryWithObjectsAndKeys:
      errorDate,@"date",
      errorMessage,@"message",
      errorCode,@"code",
    nil];
    // Use the promise rejector.
    reject(@"Error", errorMessage, [NSError errorWithDomain:@"fidel" code:500 userInfo:fidelError]);
  }];
}

@end
