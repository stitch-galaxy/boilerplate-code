#import <Foundation/Foundation.h>

#import "CSGBinaryCoding.h"
#import "CSGObjectsRegistry.h"

@protocol CSGThread <NSObject>

- (UIColor*) color;

@end

@interface CSGThread : NSObject<CSGThread>

- (id) initWithColor: (UIColor*) aColor;

- (BOOL) isEqual: (id) object;
- (BOOL) isEqualToCSGThread: (CSGThread*) aThread;
- (NSUInteger) hash;

@end


@interface CSGThread (Serialization)

- (size_t) serializedLength;
- (void) serializeWithBinaryEncoder: (CSGBinaryEncoder *) anEncoder;
+ (id) deserializeWithBinaryDecoder: (CSGBinaryDecoder*) anDecoder ObjectsRegistry: (CSGObjectsRegistry*) registry;

@end
