//
//  CSGDesignCell.m
//  Cross Stitch Library
//
//  Created by 123 on 16.03.13.
//  Copyright (c) 2013 Tarasov Evgeny. All rights reserved.
//

#import "CSGDesignCell.h"

@implementation CSGDesignCell

//Cross stitch
@synthesize crossStitch;
//Petites
@synthesize leftUpPetiteStitch;
@synthesize leftDownPetiteStitch;
@synthesize rightUpPetiteStitch;
@synthesize rightDownPetiteStitch;
//Quarter stitches
@synthesize leftUpQuarterStitch;
@synthesize leftDownQuarterStitch;
@synthesize rightUpQuarterStitch;
@synthesize rightDownQuarterStitch;
//Three quarter stitch
@synthesize leftUpThreeQuarterStitch;
@synthesize leftDownThreeQuarterStitch;
@synthesize rightUpThreeQuarterStitch;
@synthesize rightDownThreeQuarterStitch;
//HalfStitches
@synthesize slashHalfStitch;
@synthesize backslashHalfStitch;
//French knots
@synthesize frenchKnot00;
@synthesize frenchKnot01;
@synthesize frenchKnot02;
@synthesize frenchKnot10;
@synthesize frenchKnot11;
@synthesize frenchKnot12;
@synthesize frenchKnot20;
@synthesize frenchKnot21;
@synthesize frenchKnot22;


- (id)copyWithZone:(NSZone *)zone
{
    CSGDesignCell* copy = [[CSGDesignCell alloc] init];
    
    if (copy)
    {
        // DeepCopy example: NSObject subclasses
        //[copy setVendorID:[[self.vendorID copyWithZone:zone] autorelease]];
        
        // Reference copying
        copy.crossStitch = self.crossStitch;
        copy.leftUpPetiteStitch = self.leftUpPetiteStitch;
        copy.leftDownPetiteStitch = self.leftDownPetiteStitch;
        copy.rightUpPetiteStitch = self.rightUpPetiteStitch;
        copy.rightDownPetiteStitch = self.rightDownPetiteStitch;
        copy.leftUpQuarterStitch = self.leftUpQuarterStitch;
        copy.leftDownQuarterStitch = self.leftDownQuarterStitch;
        copy.rightUpQuarterStitch = self.rightUpQuarterStitch;
        copy.rightDownQuarterStitch = self.rightDownQuarterStitch;
        copy.leftUpThreeQuarterStitch = self.leftUpThreeQuarterStitch;
        copy.leftDownThreeQuarterStitch = self.leftDownThreeQuarterStitch;
        copy.rightUpThreeQuarterStitch = self.rightUpThreeQuarterStitch;
        copy.rightDownThreeQuarterStitch = self.rightDownThreeQuarterStitch;
        copy.slashHalfStitch = self.slashHalfStitch;
        copy.backslashHalfStitch = self.backslashHalfStitch;
        copy.frenchKnot00 = self.frenchKnot00;
        copy.frenchKnot01 = self.frenchKnot01;
        copy.frenchKnot02 = self.frenchKnot02;
        copy.frenchKnot10 = self.frenchKnot10;
        copy.frenchKnot11 = self.frenchKnot11;
        copy.frenchKnot12 = self.frenchKnot12;
        copy.frenchKnot20 = self.frenchKnot20;
        copy.frenchKnot21 = self.frenchKnot21;
        copy.frenchKnot22 = self.frenchKnot22;
    }
    
    return copy;
}

-(void) cleanup
{
    self.crossStitch = nil;
    self.leftUpPetiteStitch = nil;
    self.leftDownPetiteStitch = nil;
    self.rightUpPetiteStitch = nil;
    self.rightDownPetiteStitch = nil;
    self.leftUpQuarterStitch = nil;
    self.leftDownQuarterStitch = nil;
    self.rightUpQuarterStitch = nil;
    self.rightDownQuarterStitch = nil;
    self.leftUpThreeQuarterStitch = nil;
    self.leftDownThreeQuarterStitch = nil;
    self.rightUpThreeQuarterStitch = nil;
    self.rightDownThreeQuarterStitch = nil;
    self.slashHalfStitch = nil;
    self.backslashHalfStitch = nil;
    self.frenchKnot00 = nil;
    self.frenchKnot01 = nil;
    self.frenchKnot02 = nil;
    self.frenchKnot10 = nil;
    self.frenchKnot11 = nil;
    self.frenchKnot12 = nil;
    self.frenchKnot20 = nil;
    self.frenchKnot21 = nil;
    self.frenchKnot22 = nil;
}

- (id) init
{
    return self = [super init];
}

- (BOOL) isEqual: (id) object
{
    if (object == self)
    {
        return YES;
    }
    if (!object || ![object isKindOfClass:[self class]])
    {
        return NO;
    }
    
    return [self isEqualToCSGDesignCell: object];
}

- (BOOL) isEqualToCSGDesignCell: (CSGDesignCell*) aCell
{
    if (self == aCell)
    {
        return YES;
    }
    //CROSS
    if (crossStitch != NULL)
    {
        if (![crossStitch isEqual:aCell.crossStitch]) return NO;
    }
    else
    {
        if (aCell.crossStitch) return NO;
    }
    //Petites
    if (leftUpPetiteStitch != NULL)
    {
        if (![leftUpPetiteStitch isEqual:aCell.leftUpPetiteStitch]) return NO;
    }
    else
    {
        if (aCell.leftUpPetiteStitch) return NO;
    }
    
    if (leftDownPetiteStitch != NULL)
    {
        if (![leftDownPetiteStitch isEqual:aCell.leftDownPetiteStitch]) return NO;
    }
    else
    {
        if (aCell.leftDownPetiteStitch) return NO;
    }
    
    if (rightUpPetiteStitch != NULL)
    {
        if (![rightUpPetiteStitch isEqual:aCell.rightUpPetiteStitch]) return NO;
    }
    else
    {
        if (aCell.rightUpPetiteStitch) return NO;
    }
    
    if (rightDownPetiteStitch != NULL)
    {
        if (![rightDownPetiteStitch isEqual:aCell.rightDownPetiteStitch]) return NO;
    }
    else
    {
        if (aCell.rightDownPetiteStitch) return NO;
    }
    //Quarter stitches
    if (leftUpQuarterStitch != NULL)
    {
        if (![leftUpQuarterStitch isEqual:aCell.leftUpQuarterStitch]) return NO;
    }
    else
    {
        if (aCell.leftUpQuarterStitch) return NO;
    }
    
    if (leftDownQuarterStitch != NULL)
    {
        if (![leftDownQuarterStitch isEqual:aCell.leftDownQuarterStitch]) return NO;
    }
    else
    {
        if (aCell.leftDownQuarterStitch) return NO;
    }
    
    if (rightUpQuarterStitch != NULL)
    {
        if (![rightUpQuarterStitch isEqual:aCell.rightUpQuarterStitch]) return NO;
    }
    else
    {
        if (aCell.rightUpQuarterStitch) return NO;
    }
    
    if (rightDownQuarterStitch != NULL)
    {
        if (![rightDownQuarterStitch isEqual:aCell.rightDownQuarterStitch]) return NO;
    }
    else
    {
        if (aCell.rightDownQuarterStitch) return NO;
    }
    //ThreeQuarter stitches
    if (leftUpThreeQuarterStitch != NULL)
    {
        if (![leftUpThreeQuarterStitch isEqual:aCell.leftUpThreeQuarterStitch]) return NO;
    }
    else
    {
        if (aCell.leftUpThreeQuarterStitch) return NO;
    }
    
    if (leftDownThreeQuarterStitch != NULL)
    {
        if (![leftDownThreeQuarterStitch isEqual:aCell.leftDownThreeQuarterStitch]) return NO;
    }
    else
    {
        if (aCell.leftDownThreeQuarterStitch) return NO;
    }
    
    if (rightUpThreeQuarterStitch != NULL)
    {
        if (![rightUpThreeQuarterStitch isEqual:aCell.rightUpThreeQuarterStitch]) return NO;
    }
    else
    {
        if (aCell.rightUpThreeQuarterStitch) return NO;
    }
    
    if (rightDownThreeQuarterStitch != NULL)
    {
        if (![rightDownThreeQuarterStitch isEqual:aCell.rightDownThreeQuarterStitch]) return NO;
    }
    else
    {
        if (aCell.rightDownThreeQuarterStitch) return NO;
    }
    //HalfStitches
    if (slashHalfStitch != NULL)
    {
        if (![slashHalfStitch isEqual:aCell.slashHalfStitch]) return NO;
    }
    else
    {
        if (aCell.slashHalfStitch) return NO;
    }
    if (backslashHalfStitch != NULL)
    {
        if (![backslashHalfStitch isEqual:aCell.backslashHalfStitch]) return NO;
    }
    else
    {
        if (aCell.backslashHalfStitch) return NO;
    }
    //French knots
    if (frenchKnot00 != NULL)
    {
        if (![frenchKnot00 isEqual:aCell.frenchKnot00]) return NO;
    }
    else
    {
        if (aCell.frenchKnot00) return NO;
    }
    if (frenchKnot01 != NULL)
    {
        if (![frenchKnot01 isEqual:aCell.frenchKnot01]) return NO;
    }
    else
    {
        if (aCell.frenchKnot01) return NO;
    }
    if (frenchKnot02 != NULL)
    {
        if (![frenchKnot02 isEqual:aCell.frenchKnot02]) return NO;
    }
    else
    {
        if (aCell.frenchKnot02) return NO;
    }
    if (frenchKnot10 != NULL)
    {
        if (![frenchKnot10 isEqual:aCell.frenchKnot10]) return NO;
    }
    else
    {
        if (aCell.frenchKnot10) return NO;
    }
    if (frenchKnot11 != NULL)
    {
        if (![frenchKnot11 isEqual:aCell.frenchKnot11]) return NO;
    }
    else
    {
        if (aCell.frenchKnot11) return NO;
    }
    if (frenchKnot12 != NULL)
    {
        if (![frenchKnot12 isEqual:aCell.frenchKnot12]) return NO;
    }
    else
    {
        if (aCell.frenchKnot12) return NO;
    }
    if (frenchKnot20 != NULL)
    {
        if (![frenchKnot20 isEqual:aCell.frenchKnot20]) return NO;
    }
    else
    {
        if (aCell.frenchKnot20) return NO;
    }
    if (frenchKnot21 != NULL)
    {
        if (![frenchKnot21 isEqual:aCell.frenchKnot21]) return NO;
    }
    else
    {
        if (aCell.frenchKnot21) return NO;
    }
    if (frenchKnot22 != NULL)
    {
        if (![frenchKnot22 isEqual:aCell.frenchKnot22]) return NO;
    }
    else
    {
        if (aCell.frenchKnot22) return NO;
    }
    
    return YES;
}

- (NSUInteger)hash
{
    NSUInteger hash = 17;
    //cross
    hash = hash*31 + crossStitch.hash;
    //Petites
    hash = hash*31 + leftUpPetiteStitch.hash;
    hash = hash*31 + leftDownPetiteStitch.hash;
    hash = hash*31 + rightUpPetiteStitch.hash;
    hash = hash*31 + rightDownPetiteStitch.hash;
    //Quarter stitches
    hash = hash*31 + leftUpQuarterStitch.hash;
    hash = hash*31 + leftDownQuarterStitch.hash;
    hash = hash*31 + rightUpQuarterStitch.hash;
    hash = hash*31 + rightDownQuarterStitch.hash;
    //ThreeQuarter stitches
    hash = hash*31 + leftUpThreeQuarterStitch.hash;
    hash = hash*31 + leftDownThreeQuarterStitch.hash;
    hash = hash*31 + rightUpThreeQuarterStitch.hash;
    hash = hash*31 + rightDownThreeQuarterStitch.hash;
    //HalfStitches
    hash = hash*31 + slashHalfStitch.hash;
    hash = hash*31 + backslashHalfStitch.hash;
    //French knots
    hash = hash*31 + frenchKnot00.hash;
    hash = hash*31 + frenchKnot01.hash;
    hash = hash*31 + frenchKnot02.hash;
    hash = hash*31 + frenchKnot10.hash;
    hash = hash*31 + frenchKnot11.hash;
    hash = hash*31 + frenchKnot12.hash;
    hash = hash*31 + frenchKnot20.hash;
    hash = hash*31 + frenchKnot21.hash;
    hash = hash*31 + frenchKnot22.hash;
    
    return hash;
}

@end
