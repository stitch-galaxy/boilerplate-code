//
//  Stitch.m
//  Cross Stitch Library
//
//  Created by 123 on 17.02.13.
//  Copyright (c) 2013 Tarasov Evgeny. All rights reserved.
//

#import "Stitch.h"

@implementation Stitch

@synthesize Done = done;

@synthesize ThreadMaterial = threadMaterial;
@synthesize ThreadStitchType = threadStitchType;

@synthesize BeadMaterial = beadMaterial;
@synthesize BeadStitchType = beadStitchType;

- (id) initWithThreadMaterial: (ThreadMaterial*) aThreadMaterial ThreadStitchType: (int8_t) aThreadStitchType BeadMaterial: (BeadMaterial*) aBeadMaterial BeadStitchType: (int8_t) aBeadStitchType
{
    if (self = [super init])
    {
        self.ThreadMaterial = aThreadMaterial;
        self.ThreadStitchType = aThreadStitchType;
        self.BeadMaterial = aBeadMaterial;
        self.BeadStitchType = aBeadStitchType;
    }
    return self;
}


- (BOOL) isEqual: (id) object
{
    if (object == self)
    {
        return YES;
    }
    if (!object || ![object isKindOfClass: [Stitch class]])
    {
        return NO;
    }
    return [self isEqualToStitch: object];
}

- (BOOL) isEqualToStitch: (Stitch*) aStitch
{
    if (self == aStitch)
    {
        return YES;
    }
    if (![threadMaterial isEqual: aStitch.ThreadMaterial])
    {
        return NO;
    }
    if (threadStitchType != aStitch.ThreadStitchType)
    {
        return NO;
    }
    if (![threadMaterial isEqual: aStitch.ThreadMaterial])
    {
        return NO;
    }
    if (beadStitchType != aStitch.BeadStitchType)
    {
        return NO;
    }
    return YES;
}

- (NSUInteger) hash
{
    NSUInteger hash = 17;
    hash = 31 * hash + threadMaterial.hash;
    hash = 31 * hash + threadStitchType;
    hash = 31 * hash + beadMaterial.hash;
    hash = 31 * hash + beadStitchType;
    return hash;
}

@end


@implementation Stitch (Serialization)

- (size_t) GetSerializedLength
{
    return sizeof(int8_t) + sizeof(int8_t) + sizeof(uint32_t) + sizeof(uint32_t) + sizeof(BOOL);
}

- (void) SerializeToBuffer: (void*) buffer WithThreadsCollection: (ThreadMaterialCollection*) threads AndBeadsCollection: (BeadMaterialCollection*) beads
{
    uint8_t *buf = (uint8_t*) buffer;
    *buf = threadStitchType;
    
    ++buf;
    *buf = beadStitchType;
    
    ++buf;
    uint32_t *iBuf = (uint32_t *) buf;
    uint32_t threadMaterialsIndex = [threads GetThreadMaterialIndex: self.ThreadMaterial];
    *iBuf = threadMaterialsIndex;
    
    uint32_t beadMaterialsIndex = [beads GetBeadMaterialIndex: self.BeadMaterial];
    ++iBuf;
    *iBuf = beadMaterialsIndex;
}

+ (id) DeserializeFromBuffer: (void*) buffer WithThreadsCollection: (ThreadMaterialCollection*) threads AndBeadsCollection: (BeadMaterialCollection*) beads
{
    uint8_t *buf = (uint8_t*) buffer;
    int8_t aThreadStitchType = *buf;
    
    ++buf;
    int8_t aBeadStitchType = *buf;
    
    ++buf;
    uint32_t *iBuf = (uint32_t *) buf;
    uint32_t threadMaterialsIndex = *iBuf;
    ThreadMaterial* aThreadMaterial = [threads GetThreadMaterialByIndex:threadMaterialsIndex];
    
    ++iBuf;
    uint32_t beadMaterialsIndex = *iBuf;
    BeadMaterial* beadMaterial = [beads GetBeadMaterialByIndex:beadMaterialsIndex];
    
    return [[Stitch alloc] initWithThreadMaterial:aThreadMaterial ThreadStitchType:aThreadStitchType BeadMaterial:beadMaterial BeadStitchType:aBeadStitchType];
}

@end